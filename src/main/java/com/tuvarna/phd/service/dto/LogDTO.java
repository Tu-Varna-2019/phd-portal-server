package com.tuvarna.phd.service.dto;

import io.smallrye.common.constraint.NotNull;
import java.security.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "LogDTO", description = "Log DTO")
@Data
@AllArgsConstructor
public class LogDTO {

  @NotNull
  @Schema(title = "first_name", required = true)
  private String description;

  @NotNull
  @Schema(title = "middle_name", required = true)
  private String user;

  @NotNull
  @Schema(title = "timestamp", required = true)
  private Timestamp timestamp;

  @NotNull
  @Schema(title = "action", required = true)
  private String action;

  @NotNull
  @Schema(title = "role", required = true)
  private String role;
}
