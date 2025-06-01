package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ExamDTO", description = "Exam DTO")
@Data
@AllArgsConstructor
public class ExamDTO {

  @NotNull
  @Schema(title = "grade", required = true)
  private Double grade;
}
