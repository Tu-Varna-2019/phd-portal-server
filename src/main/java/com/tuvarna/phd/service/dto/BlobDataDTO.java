package com.tuvarna.phd.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.smallrye.common.constraint.NotNull;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Schema(name = "BlobDTO", description = "Blob DTO")
@Getter
@Setter
public class BlobDataDTO {

  @NotNull
  @RestForm
  @Schema(title = "data", required = true)
  public FileUpload data;

  @PartType(MediaType.TEXT_PLAIN)
  @RestForm
  @NotNull
  @Schema(title = "filename", required = true)
  public String filename;

  @PartType(MediaType.TEXT_PLAIN)
  @RestForm
  @NotNull
  @Schema(title = "mimetype", required = true)
  public String mimetype;

  @JsonIgnore @Nullable public String uniqueFilename;

  public void generateUniqueFilename() {
    String extension = filename.substring(filename.lastIndexOf("."), filename.length());

    this.uniqueFilename = UUID.randomUUID().toString() + extension;
  }
}
