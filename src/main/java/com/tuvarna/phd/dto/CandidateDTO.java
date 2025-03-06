package com.tuvarna.phd.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CandidateDTO", description = "Candidate DTO")
@Data
@AllArgsConstructor
public class CandidateDTO {

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String email;

  @NotNull
  @Schema(title = "country", required = true)
  private String country;

  @NotNull
  @Schema(title = "city", required = true)
  private String city;

  @NotNull
  @Schema(title = "address", required = true)
  private String address;

  @NotNull
  @Schema(title = "biography", required = true)
  private FileBlobDTO biography;

  @NotNull
  @Schema(title = "pin", required = true)
  private String pin;

  @NotNull
  @Schema(title = "curriculum", required = true)
  private CurriculumDTO curriculum;

  @NotNull
  @Schema(title = "faculty", required = true)
  private String faculty;
}
