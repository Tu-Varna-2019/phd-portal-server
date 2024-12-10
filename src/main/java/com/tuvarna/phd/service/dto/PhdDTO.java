package com.tuvarna.phd.service.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PhdDTO", description = "PHD DTO")
@Data
public class PhdDTO {

  @NotNull
  @Schema(title = "oid", required = true)
  private String oid;

  @NotNull
  @Schema(title = "first_name", required = true)
  private String firstName;

  @NotNull
  @Schema(title = "middle_name", required = true)
  private String middleName;

  @NotNull
  @Schema(title = "last_name", required = true)
  private String lastName;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;
}
