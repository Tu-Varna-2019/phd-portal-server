package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CommissionRequestDTO", description = "Commission request DTO")
@Data
@AllArgsConstructor
public class CommissionRequestDTO {

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "committees", required = true)
  private List<CommitteeRequestDTO> committees;
}
