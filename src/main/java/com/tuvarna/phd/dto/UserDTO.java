package com.tuvarna.phd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UserDTO", description = "User DTO")
@Data
@AllArgsConstructor
public class UserDTO {

  @NotNull
  @Schema(title = "oid", required = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String oid;

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;

  @NotNull
  @Schema(title = "pin", required = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String pin;

  @Schema(title = "group", required = false)
  private String group;

  @Schema(title = "pictureBlob", required = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String pictureBlob;

  public UserDTO(String name, String email, String pin, String group) {
    this.name = name;
    this.email = email;
    this.pin = pin;
    this.group = group;
  }

  public UserDTO(String oid, String name, String email, String pin, String group) {
    this.oid = oid;
    this.name = name;
    this.email = email;
    this.pin = pin;
    this.group = group;
  }
}
