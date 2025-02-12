package com.tuvarna.phd.service;

import com.tuvarna.phd.service.dto.BlobDataDTO;
import com.tuvarna.phd.service.dto.FileBlobDTO;

public sealed interface S3ClientService permits S3ClientServiceImpl {

  void upload(BlobDataDTO file, String oid, String type);

  void delete(String oid, String group, String type, String filename);

  void setPictureByOid(String uniqueFilename, String group, String oid);

  String getPictureByOid(String group, String oid);

  FileBlobDTO download(String objectKey);
}
