package com.tuvarna.phd.service.dto.storage;

import jakarta.ws.rs.core.MediaType;
import java.io.File;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

public class BlobDataDTO {

  @RestForm("file")
  public File data;

  @RestForm
  @PartType(MediaType.TEXT_PLAIN)
  public String filename;

  @RestForm
  @PartType(MediaType.TEXT_PLAIN)
  public String mimetype;
}
