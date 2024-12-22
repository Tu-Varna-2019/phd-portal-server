package com.tuvarna.phd.service.dto;

import io.smallrye.common.constraint.NotNull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UserDTO", description = "User DTO")
@Data
@AllArgsConstructor
public class UserDTO {

  @NotNull
  @Schema(title = "oid", required = true)
  private String oid;

  @Nullable
  @Schema(title = "name", required = false)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;
}
