package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PhdDTO", description = "PHD DTO")
@Data
@AllArgsConstructor
public class PhdDTO {

  @NotNull
  @Schema(title = "oid", required = true)
  private String oid;

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "picture", required = true)
  private String picture;

  @Nullable
  @Schema(title = "country", required = false)
  private String country;

  @Nullable
  @Schema(title = "city", required = false)
  private String city;

  @Nullable
  @Schema(title = "address", required = false)
  private String address;

  @Nullable
  @Schema(title = "pin", required = false)
  private String pin;

  @Schema(title = "dissertation", required = false)
  private String dissertation;

  @Schema(title = "status", required = false)
  private String status;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;

  public PhdDTO(String name, String email) {
    this.name = name;
    this.email = email;
  }
}
