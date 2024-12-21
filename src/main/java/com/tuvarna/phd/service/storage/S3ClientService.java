package com.tuvarna.phd.service.storage;

import com.tuvarna.phd.service.dto.storage.BlobDataDTO;
import io.smallrye.mutiny.tuples.Tuple2;

public interface S3ClientService {

  void upload(BlobDataDTO blob);

  Tuple2<byte[], String> download(String key);
}
