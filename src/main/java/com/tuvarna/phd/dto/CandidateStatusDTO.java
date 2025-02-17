package com.tuvarna.phd.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CandidateStatusDTO", description = "Candidate status DTO")
@Data
@AllArgsConstructor
public class CandidateStatusDTO {

  @Nullable
  @Schema(title = "status", required = true)
  private String status;

  @Nullable
  @Schema(title = "email", required = true)
  private String email;
}
