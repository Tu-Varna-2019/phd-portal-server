package com.tuvarna.phd.service.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import lombok.Getter;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "DoctoralCenterPasswordChangeDTO", description = "Doctoral Center Password change DTO")
@Data
@Getter
public class DoctoralCenterPasswordChangeDTO {

  @NotNull
  @Schema(title = "email", required = true)
  private String email;

  @NotNull
  @Schema(title = "oldPassword", required = true)
  private String oldPassword;

  @NotNull
  @Schema(title = "newPassword", required = true)
  private String newPassword;
}
