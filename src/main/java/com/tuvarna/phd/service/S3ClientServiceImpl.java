package com.tuvarna.phd.service;

import com.tuvarna.phd.exception.BlobException;
import com.tuvarna.phd.exception.S3ClientException;
import com.tuvarna.phd.service.dto.BlobDataDTO;
import com.tuvarna.phd.service.dto.FileBlobDTO;
import io.vertx.ext.auth.NoSuchKeyIdException;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.Map;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@ApplicationScoped
public final class S3ClientServiceImpl implements S3ClientService {
  private final PgPool pgClient;
  @Inject S3Client client;
  @Inject private Logger LOG;

  @ConfigProperty(name = "bucket.name")
  String bucketName;

  private Map<String, String> DEFAULT_IMAGE_NAMES =
      Collections.unmodifiableMap(
          Map.of(
              "phd", "phd_image.png",
              "committee", "committee_image.png",
              "doctoralCenter", "doctoralCenter_image.png"));

  public S3ClientServiceImpl(PgPool pgPool) {
    this.pgClient = pgPool;
  }

  @Override
  public void setPictureByOid(String imageName, String group, String oid) {
    LOG.info("Now updating the user image with this name: " + imageName);
    StringBuilder statement = new StringBuilder();

    switch (group) {
      case "phd", "committee", "doctoralCenter" ->
          statement.append("UPDATE " + group.toLowerCase() + " SET picture = $1 WHERE oid = $2");
      default ->
          throw new S3ClientException(
              "Unable to change user picture. The group is incorrect: " + group);
    }

    this.pgClient
        .preparedQuery(statement.toString())
        .execute(Tuple.of(imageName, oid))
        .await()
        .indefinitely();
    LOG.info("Image updated with picture: " + imageName);
  }

  @Override
  public void upload(BlobDataDTO file, String oid, String type) {
    LOG.info(
        "Received a service request to upload file: "
            + file.getFilename()
            + " with mimeType: "
            + file.getMimetype()
            + " for type: "
            + type);

    // NOTE: Check if  mimetype is defined
    if (file.getMimetype() == null || file.getMimetype().isEmpty())
      throw new BlobException("File extension for file not found!", 415);

    LOG.info("Saving file with unique name: " + file.getUniqueFilename());

    PutObjectResponse pResponse =
        this.client.putObject(
            this.buildPutRequest(
                oid + "/" + type + "/" + file.getUniqueFilename(), file.getMimetype()),
            RequestBody.fromFile(file.getData().filePath()));

    if (pResponse == null) throw new BlobException("Error in uploading file!");
  }

  @Override
  public FileBlobDTO download(String objectKey) {
    LOG.info("Received a service request to download an s3 file path: " + objectKey);

    try {
      ResponseBytes<GetObjectResponse> oBytes =
          this.client.getObjectAsBytes(this.buildGetRequest(objectKey));

      LOG.info("Download success!");

      return new FileBlobDTO(oBytes.asByteArray(), oBytes.response().contentType());

    } catch (NoSuchKeyIdException exception) {
      LOG.error("Unable to find file in path: " + objectKey);
      throw new S3ClientException(
          "Unable to find file in path: " + objectKey + " with exception: " + exception);
    }
  }

  @Override
  public void delete(String oid, String group, String type, String filename) {
    LOG.info("Received a service request to delete a file: " + filename);
    try {
      this.client.deleteObject(this.buildDeleteRequest(oid + "/" + type + "/" + filename));

      if (type.equals("avatar")) {
        this.setPictureByOid(this.DEFAULT_IMAGE_NAMES.get(group), group, oid);
      }

      LOG.info("Succeess! File: " + filename + " deleted!");
    } catch (S3Exception exception) {
      LOG.error("Error in deleting object");
      throw new S3ClientException("Error in deleting object");
    }
  }

  private DeleteObjectRequest buildDeleteRequest(String objectName) {
    return DeleteObjectRequest.builder().bucket(bucketName).key(objectName).build();
  }

  private PutObjectRequest buildPutRequest(String objectName, String mimetype) {
    return PutObjectRequest.builder()
        .bucket(bucketName)
        .key(objectName)
        .contentType(mimetype)
        .build();
  }

  private GetObjectRequest buildGetRequest(String objectKey) {
    return GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
  }
}
