package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "IdDTO", description = "Id DTO")
@Data
@AllArgsConstructor
public class IdDTO {

  @NotNull
  @Schema(title = "id", required = true)
  private Long id;
}
