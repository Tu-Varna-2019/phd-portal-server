package com.tuvarna.phd.service.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CommitteeDTO", description = "Committee DTO")
@Data
public class CommitteeDTO {

  @NotNull
  @Schema(title = "oid", required = true)
  private String oid;

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;
}
