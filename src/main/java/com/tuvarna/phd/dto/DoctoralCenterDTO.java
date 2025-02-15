package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "DoctoralCenterDTO", description = "Doctoral Center DTO")
@Data
public class DoctoralCenterDTO {

  @NotNull
  @Schema(title = "oid", required = true)
  private String oid;

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;

  @NotNull
  @Schema(title = "picture", required = true)
  private String picture;

  @NotNull
  @Schema(title = "role", required = false)
  private String role;
}
