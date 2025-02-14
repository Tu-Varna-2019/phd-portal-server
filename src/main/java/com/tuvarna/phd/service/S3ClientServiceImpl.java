package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.BlobDataDTO;
import com.tuvarna.phd.dto.FileBlobDTO;
import com.tuvarna.phd.exception.S3ClientException;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.S3Model;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class S3ClientServiceImpl implements S3ClientService {
  DatabaseModel databaseModel;
  S3Model s3Model;
  @Inject private Logger LOG;

  @ConfigProperty(name = "bucket.name")
  String bucketName;

  public S3ClientServiceImpl(DatabaseModel databaseModel, S3Model s3Model) {
    this.databaseModel = databaseModel;
    this.s3Model = s3Model;
  }

  @Override
  public void setPictureByOid(String picture, String group, String oid) {
    LOG.info("Now updating the user picture with name: " + picture);

    String statement =
        switch (group) {
          case "phd", "committee", "doctoralCenter" -> {
            yield "UPDATE " + group.toLowerCase() + " SET picture = $1 WHERE oid = $2";
          }
          default -> {
            throw new S3ClientException(
                "Unable to change user picture. The group is incorrect: " + group);
          }
        };

    this.databaseModel.execute(statement, Tuple.of(picture, oid));
    LOG.info("Picture updated with picture: " + picture);
  }

  @Override
  public String getPictureByOid(String group, String oid) {
    LOG.info("Retrieveing picture name for user oid: " + oid);
    String statement = "SELECT picture FROM " + group.toLowerCase() + " WHERE oid = $1";

    return this.databaseModel.selectString(statement, Tuple.of(oid), "picture");
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

    this.s3Model.uploadBlob(oid + "/" + type + "/" + file.getUniqueFilename(), file);

    LOG.info("Saving file with unique name: " + file.getUniqueFilename());
  }

  @Override
  public FileBlobDTO download(String objectName) {
    LOG.info("Received a service request to download an s3 file path: " + objectName);
    FileBlobDTO fileBlobDTO = this.s3Model.getBlob(objectName);
    LOG.info("Download success!");

    return fileBlobDTO;
  }

  @Override
  public void delete(String oid, String group, String type, String filename) {
    LOG.info("Received a service request to delete a file: " + filename);
    this.s3Model.deleteObject(oid + "/" + type + "/" + filename);
    LOG.info("Succeess! File: " + filename + " deleted!");
  }
}
