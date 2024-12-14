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
  @Schema(title = "description", required = true)
  private String description;

  @NotNull
  @Schema(title = "timestamp", required = true)
  private Timestamp timestamp;

  @NotNull
  @Schema(title = "action", required = true)
  private String action;

  @NotNull
  @Schema(title = "status", required = true)
  /* Status:
   * SUCCESS
   * FAILURE
   */
  private String status;

  @NotNull
  @Schema(title = "userPrincipal", required = true)
  private UserPrincipalDTO userPrincipalDTO;
}
