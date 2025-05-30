package com.tuvarna.phd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CandidateDTO", description = "Candidate DTO")
@Data
@AllArgsConstructor
public class CandidateDTO {

  @NotNull
  @Schema(title = "name", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String name;

  @NotNull
  @Schema(title = "email", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String email;

  @NotNull
  @Schema(title = "exam_step", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty("exam_step")
  private String examStep;

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
  @Schema(title = "post_code", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty("post_code")
  private String postCode;

  @NotNull
  @Schema(title = "yearAccepted", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long yearAccepted;

  @NotNull
  @Schema(title = "biography", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String biography;

  @Nullable
  @Schema(title = "curriculum", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String curriculum;

  @NotNull
  @Schema(title = "faculty", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String faculty;

  @NotNull
  @Schema(title = "status", required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String status;
}
