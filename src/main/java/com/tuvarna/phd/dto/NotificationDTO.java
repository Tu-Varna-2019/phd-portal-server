package com.tuvarna.phd.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.smallrye.common.constraint.NotNull;
import jakarta.annotation.Nullable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "NotificationDTO", description = "Notification DTO")
@Data
@AllArgsConstructor
public class NotificationDTO {

  @NotNull
  @Schema(title = "title", required = true)
  private String title;

  @Schema(title = "description", required = true)
  private String description;

  @NotNull
  @Schema(title = "severity", required = true)
  private String severity;

  @JsonIgnore
  @Schema(title = "creation", required = true)
  private Timestamp creation;

  @Schema(title = "scope", required = true)
  private String scope;

  @Nullable
  @Schema(title = "group", required = true)
  private String group;

  @Nullable
  @Schema(title = "recipients", required = true)
  private List<String> recipients;

  public void addRecipients(List<String> recipients) {
    if (this.recipients == null) this.recipients = new ArrayList<>(recipients.size());

    this.recipients.addAll(recipients);
  }
}
