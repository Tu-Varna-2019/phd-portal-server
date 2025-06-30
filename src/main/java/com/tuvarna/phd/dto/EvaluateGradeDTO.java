package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "EvaluateGradeDTO", description = "Evaluate grade DTO")
@Data
@AllArgsConstructor
public class EvaluateGradeDTO {

  @NotNull
  @Schema(title = "grade", required = true)
  private Double grade;

  @NotNull
  @Schema(title = "subject", required = true)
  private String subject;

  @NotNull
  @Schema(title = "pin", required = true)
  private String pin;
}
