package com.tuvarna.phd.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.smallrye.common.constraint.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "LogDTO", description = "Log DTO")
@Data
@AllArgsConstructor
public class LogDTO {

  private enum LogLevel {
    INFO,
    SUCCESS,
    ERROR,
    WARN
  }

  @NotNull
  @Schema(title = "description", required = true)
  private String description;

  @NotNull
  @Schema(title = "timestamp", required = true)
  private Instant timestamp;

  @NotNull
  @Schema(title = "action", required = true)
  private String action;

  @NotNull
  @Schema(title = "level", required = true)
  private LogLevel level;

  @NotNull
  @Schema(title = "user", required = true)
  @JsonProperty("user")
  private UserPrincipalDTO userPrincipalDTO;
}
