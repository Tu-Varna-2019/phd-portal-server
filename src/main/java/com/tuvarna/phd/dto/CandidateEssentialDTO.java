package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CandidateEssentialDTO", description = "CandidateEssential DTO")
@Data
@AllArgsConstructor
public class CandidateEssentialDTO {

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;

  @NotNull
  @Schema(title = "status", required = true)
  private String status;
}
