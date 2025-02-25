package com.tuvarna.phd.model;

import com.tuvarna.phd.dto.BlobDataDTO;
import com.tuvarna.phd.dto.FileBlobDTO;
import com.tuvarna.phd.exception.HttpException;
import io.vertx.ext.auth.NoSuchKeyIdException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Base64;
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
public class S3Model {
  @Inject S3Client client;

  @ConfigProperty(name = "bucket.name")
  String bucketName;

  @Inject private Logger LOG;

  public String getDataUrlPicture(String oid, String picture) {

    ResponseBytes<GetObjectResponse> oBytes =
        this.client.getObjectAsBytes(this.buildGetRequest(oid + "/avatar/" + picture));

    String pictureBase64 = Base64.getEncoder().encodeToString(oBytes.asByteArray());
    String mimeType = oBytes.response().contentType();
    // TODO: Not sure if the data url must be constructed on the server side and not the client side
    String dataUrl = "data:" + mimeType + ";base64," + pictureBase64;

    return dataUrl;
  }

  private GetObjectRequest buildGetRequest(String objectKey) {
    return GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
  }

  private PutObjectRequest buildPutRequest(String objectName, String mimetype) {
    return PutObjectRequest.builder()
        .bucket(bucketName)
        .key(objectName)
        .contentType(mimetype)
        .build();
  }

  private DeleteObjectRequest buildDeleteRequest(String objectName) {
    return DeleteObjectRequest.builder().bucket(bucketName).key(objectName).build();
  }

  public void deleteObject(String objectName) {
    try {
      this.client.deleteObject(this.buildDeleteRequest(objectName));

    } catch (S3Exception exception) {
      LOG.error("Error in deleting object");
      throw new HttpException("Error in deleting object");
    }
  }

  public FileBlobDTO getBlob(String objectName) {
    try {
      ResponseBytes<GetObjectResponse> oResponseBytes =
          this.client.getObjectAsBytes(this.buildGetRequest(objectName));
      FileBlobDTO fileBlobDTO =
          new FileBlobDTO(oResponseBytes.asByteArray(), oResponseBytes.response().contentType());

      return fileBlobDTO;
    } catch (NoSuchKeyIdException exception) {
      LOG.error("Unable to find file in path: " + objectName);
      throw new HttpException(
          "Unable to find file in path: " + objectName + " with exception: " + exception);
    }
  }

  public void uploadBlob(String objectName, BlobDataDTO file) {
    if (file.getMimetype() == null || file.getMimetype().isEmpty())
      throw new HttpException("File extension for file not found!", 415);

    PutObjectResponse pResponse =
        this.client.putObject(
            this.buildPutRequest(objectName, file.getMimetype()),
            RequestBody.fromFile(file.getData().filePath()));

    if (pResponse == null) throw new HttpException("Error in uploading file!");
  }
}
