package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CurriculumDTO", description = "Curriculum DTO")
@Data
@AllArgsConstructor
public class CurriculumDTO {

  @NotNull
  @Schema(title = "id", required = true)
  private Long id;

  @NotNull
  @Schema(title = "description", required = true)
  private String description;

  @NotNull
  @Schema(title = "mode", required = true)
  private String mode;
}
