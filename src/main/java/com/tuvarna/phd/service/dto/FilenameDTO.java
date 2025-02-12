package com.tuvarna.phd.service.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "FilenameDTO", description = "Filename DTO")
@Getter
@Data
@AllArgsConstructor
public class FilenameDTO {

  @NotNull
  @Schema(title = "filename", required = true)
  private String filename;
}
