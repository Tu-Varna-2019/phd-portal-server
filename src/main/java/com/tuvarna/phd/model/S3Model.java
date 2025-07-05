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
  @Inject private Logger LOG;

  @ConfigProperty(name = "bucket.name")
  String bucketName;

  // TODO: Let the client build data url
  public String getDataUrlPicture(String oid, String picture) {
    // NOTE: Make it return "" becase the client profile is not showing
    if (picture == null || picture.strip().isEmpty()) return "";

    ResponseBytes<GetObjectResponse> oBytes =
        this.client.getObjectAsBytes(this.buildGetRequest(oid + "/avatar/" + picture));

    String pictureBase64 = Base64.getEncoder().encodeToString(oBytes.asByteArray());
    String mimeType = oBytes.response().contentType();
    String dataUrl = "data:" + mimeType + ";base64," + pictureBase64;

    return dataUrl;
  }

  public String createDataUrl(byte[] oBytes, String mimeType) {
    String base64 = Base64.getEncoder().encodeToString(oBytes);

    return "data:" + mimeType + ";base64," + base64;
  }

  private GetObjectRequest buildGetRequest(String key) {
    return GetObjectRequest.builder().bucket(bucketName).key(key).build();
  }

  private PutObjectRequest buildPutRequest(String key, String mimetype) {
    return PutObjectRequest.builder().bucket(bucketName).key(key).contentType(mimetype).build();
  }

  private DeleteObjectRequest buildDeleteRequest(String key) {
    return DeleteObjectRequest.builder().bucket(bucketName).key(key).build();
  }

  public void deleteObject(String key) {
    try {
      this.client.deleteObject(this.buildDeleteRequest(key));

    } catch (S3Exception exception) {
      LOG.error("Error in deleting object");
      throw new HttpException("Error in deleting object");
    }
  }

  public FileBlobDTO getBlob(String key) {
    try {
      ResponseBytes<GetObjectResponse> oResponseBytes =
          this.client.getObjectAsBytes(this.buildGetRequest(key));

      FileBlobDTO fileBlobDTO =
          new FileBlobDTO(oResponseBytes.asByteArray(), oResponseBytes.response().contentType());

      return fileBlobDTO;
    } catch (NoSuchKeyIdException exception) {
      LOG.error("Unable to find file in path: " + key);
      throw new HttpException(
          "Unable to find file in path: " + key + " with exception: " + exception);
    }
  }

  public void uploadBlob(String key, BlobDataDTO file) {
    if (file.getMimetype() == null || file.getMimetype().isEmpty())
      throw new HttpException("File extension for file not found!", 415);

    PutObjectResponse pResponse =
        this.client.putObject(
            this.buildPutRequest(key, file.getMimetype()),
            RequestBody.fromFile(file.getData().filePath()));

    if (pResponse == null) throw new HttpException("Error in uploading file!");
  }
}
