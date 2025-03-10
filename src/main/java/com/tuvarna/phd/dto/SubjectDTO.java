package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SubjectDTO", description = "Subject DTO")
@Data
@AllArgsConstructor
public class SubjectDTO {
  @NotNull
  @Schema(title = "name", required = true)
  private String name;
}
