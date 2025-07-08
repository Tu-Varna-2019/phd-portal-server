package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ReportResponseDTO", description = "Report response DTO")
@Data
@AllArgsConstructor
public class ReportResponseDTO {

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "conduct", required = true)
  private String conduct;

  @NotNull
  @Schema(title = "enrollmentDate", required = true)
  private Date enrollmentDate;

  @NotNull
  @Schema(title = "orderNumber", required = true)
  private Integer orderNumber;
}
