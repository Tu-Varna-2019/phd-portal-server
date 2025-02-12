package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "FileBlobDTO", description = "FileBlob DTO")
@Data
@Getter
@AllArgsConstructor
public class FileBlobDTO {

  @NotNull
  @Schema(title = "data", required = true)
  private byte[] data;

  @NotNull
  @Schema(title = "mimetype", required = true)
  private String mimetype;
}
