package com.tuvarna.phd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "mode", required = true)
  private String mode;

  @NotNull
  @Schema(title = "year_period", required = true)
  private String yearPeriod;

  @NotNull
  @Schema(title = "faculty", required = true)
  private String faculty;

  @NotNull
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(title = "subjects", required = true)
  private List<String> subjects;
}
