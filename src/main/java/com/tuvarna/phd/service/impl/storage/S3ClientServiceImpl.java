package com.tuvarna.phd.service.impl.storage;

import com.tuvarna.phd.exception.BlobException;
import com.tuvarna.phd.service.dto.storage.BlobDataDTO;
import com.tuvarna.phd.service.storage.S3ClientService;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ApplicationScoped
public class S3ClientServiceImpl implements S3ClientService {
  @Inject S3Client client;

  @ConfigProperty(name = "bucket.name")
  String bucketName;

  @Inject private Logger LOG;

  @Override
  public void upload(BlobDataDTO blob) {
    LOG.info("Received a service request to upload file");

    // NOTE: Check if  mimetype is defined
    if (blob.mimetype == null || blob.mimetype.isEmpty())
      throw new BlobException("File extension for file not found!", 415);

    PutObjectResponse pResponse =
        this.client.putObject(this.buildPutRequest(blob), RequestBody.fromFile(blob.data));

    if (pResponse == null) throw new BlobException("Error in uploading file!");
  }

  @Override
  public Tuple2<byte[], String> download(String key) {

    ResponseBytes<GetObjectResponse> oBytes =
        this.client.getObjectAsBytes(this.buildGetRequest(key));

    return Tuple2.of(oBytes.asByteArray(), oBytes.response().contentType());
  }

  private PutObjectRequest buildPutRequest(BlobDataDTO blob) {
    return PutObjectRequest.builder()
        .bucket(bucketName)
        .key(blob.filename)
        .contentType(blob.mimetype)
        .build();
  }

  private GetObjectRequest buildGetRequest(String objectKey) {
    return GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
  }
}
