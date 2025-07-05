package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CommitteeRequestDTO", description = "Committee request DTO")
@Data
@AllArgsConstructor
public class CommitteeRequestDTO {
  @NotNull
  @Schema(title = "oid", required = true)
  private String oid;
}
