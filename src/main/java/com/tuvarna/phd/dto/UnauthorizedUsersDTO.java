package com.tuvarna.phd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.smallrye.common.constraint.NotNull;
import java.sql.Timestamp;
import javax.annotation.Nullable;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UnauthorizedUsersDTO", description = "UnauthorizedUsers DTO")
@Data
public class UnauthorizedUsersDTO {

  @NotNull
  @Schema(title = "oid", required = true)
  private String oid;

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;

  @Nullable
  @Schema(title = "group", required = true)
  private String group;

  @NotNull
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss.SSS",
      timezone = "UTC")
  @Schema(title = "timestamp", required = true)
  private Timestamp timestamp;
}
