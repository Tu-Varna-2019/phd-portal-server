package com.tuvarna.phd.service.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CandidateDTO", description = "PHD DTO")
@Data
@AllArgsConstructor
public class CandidateDTO {

  @Nullable
  @Schema(title = "status", required = true)
  private String status;

  @Nullable
  @Schema(title = "email", required = true)
  private String email;
}
