package com.tuvarna.phd.service.dto;

import io.smallrye.common.constraint.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "sendLogDTO", description = "Log DTO")
@Data
@AllArgsConstructor
public class sendLogDTO {

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
  private String level;
}
