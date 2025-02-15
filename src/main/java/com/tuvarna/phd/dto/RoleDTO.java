package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "RoleDTO", description = "Role DTO")
@Data
public class RoleDTO {

  @NotNull
  @Schema(title = "role", required = true)
  private String role;
}
