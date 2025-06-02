package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "NameDTO", description = "Name DTO")
@Data
@AllArgsConstructor
public class NameDTO {

  @NotNull
  @Schema(title = "Name", required = true)
  private String name;
}
