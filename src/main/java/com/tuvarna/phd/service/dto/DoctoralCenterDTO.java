package com.tuvarna.phd.service.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "DoctoralCenterDTO", description = "Doctoral Center DTO")
@Data
public class DoctoralCenterDTO {

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;

  @NotNull
  @Schema(title = "role", required = true)
  private String role;
}
