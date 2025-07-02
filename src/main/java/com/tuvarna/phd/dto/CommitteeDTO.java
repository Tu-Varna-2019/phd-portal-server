package com.tuvarna.phd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CommitteeDTO", description = "Committee DTO")
@Data
@AllArgsConstructor
public class CommitteeDTO {
  @NotNull
  @Schema(title = "oid", required = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String oid;

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  @NotNull
  @Schema(title = "email", required = true)
  private String picture;

  @NotNull
  @Schema(title = "grade", required = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double grade;

  @NotNull
  @Schema(title = "faculty", required = true)
  private String faculty;

  @NotNull
  @Schema(title = "role", required = true)
  private String role;

  public CommitteeDTO(String oid, String name, String picture, String faculty, String role) {
    this.oid = oid;
    this.name = name;
    this.picture = picture;
    this.faculty = faculty;
    this.role = role;
  }
}
