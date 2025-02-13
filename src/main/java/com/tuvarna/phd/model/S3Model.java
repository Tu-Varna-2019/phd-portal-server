package com.tuvarna.phd.model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Base64;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@ApplicationScoped
public class S3Model {
  @Inject S3Client client;

  @ConfigProperty(name = "bucket.name")
  String bucketName;

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
}
