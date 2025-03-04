package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CurriculumDTO", description = "Curriculum DTO")
@Data
@AllArgsConstructor
public class CurriculumDTO {
  @NotNull
  @Schema(title = "description", required = true)
  private String description;

  @NotNull
  @Schema(title = "yearPeriod", required = true)
  private Long yearPeriod;

  @NotNull
  @Schema(title = "mode", required = true)
  private String mode;

  @NotNull
  @Schema(title = "subjects", required = true)
  private List<String> subjects;
}
