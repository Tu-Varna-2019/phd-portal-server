package com.tuvarna.phd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.Nullable;
import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CandidateApplyDTO", description = "Candidate apply DTO")
@Data
@AllArgsConstructor
public class CandidateApplyDTO {

  @NotNull
  @Schema(title = "name", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String name;

  @NotNull
  @Schema(title = "email", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String email;

  @NotNull
  @Schema(title = "pin", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String pin;

  @NotNull
  @Schema(title = "country", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String country;

  @NotNull
  @Schema(title = "city", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String city;

  @NotNull
  @Schema(title = "address", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String address;

  @NotNull
  @Schema(title = "biography", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String biography;

  @NotNull
  @Schema(title = "yearAccepted", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long yearAccepted;

  @NotNull
  @Schema(title = "post_code", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long postCode;

  @Nullable
  @Schema(title = "curriculum", required = false)
  private CurriculumCreateDTO curriculum;

  @NotNull
  @Schema(title = "faculty", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String faculty;

  @NotNull
  @Schema(title = "status", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String status;
}
