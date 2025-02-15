package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "NotificationClientDTO", description = "Notification DTO")
@Data
@AllArgsConstructor
public class NotificationClientDTO {

  @NotNull
  @Schema(title = "id", required = true)
  private Long id;

  @NotNull
  @Schema(title = "title", required = true)
  private String title;

  @Schema(title = "description", required = true)
  private String description;

  @NotNull
  @Schema(title = "severity", required = true)
  private String severity;

  @Schema(title = "creation", required = true)
  private Timestamp creation;
}
