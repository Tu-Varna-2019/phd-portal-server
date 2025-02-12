package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UserPrincipalDTO", description = "User Principal DTO")
@Data
@AllArgsConstructor
public class UserPrincipalDTO {

  @Nullable
  @Schema(title = "oid", required = false)
  private String oid;

  @Nullable
  @Schema(title = "name", required = false)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;

  @NotNull
  @Schema(title = "group", required = true)
  private String group;
}
