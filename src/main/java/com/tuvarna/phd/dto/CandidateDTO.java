package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CandidateDTO", description = "Candidate DTO")
@Data
@AllArgsConstructor
public class CandidateDTO {
  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;

  @NotNull
  @Schema(title = "biography", required = true)
  private String biography;

  @NotNull
  @Schema(title = "status", required = true)
  private String status;
}
