package com.tuvarna.phd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.smallrye.common.constraint.NotNull;
import java.sql.Date;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "GradeDTO", description = "Grade DTO")
@Data
@AllArgsConstructor
public class GradeDTO {

  @NotNull
  @Schema(title = "id", required = true)
  private Long id;

  @NotNull
  @Schema(title = "grade", required = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double grade;

  @NotNull
  @Schema(title = "eval_date", required = true)
  private Date evalDate;

  @NotNull
  @Schema(title = "commision", required = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private CommisionDTO commission;

  @NotNull
  @Schema(title = "report", required = true)
  private String report;

  @Nullable
  @Schema(title = "attachments", required = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Set<String> attachments;

  @Nullable
  @Schema(title = "evaluatedUser", required = true)
  private UserDTO evaluatedUser;

  @Nullable
  @Schema(title = "subject", required = true)
  private String subject;

  public GradeDTO(
      Long id,
      Double grade,
      Date evalDate,
      String report,
      Set<String> attachments,
      UserDTO evaluatedUser,
      String subject) {
    this.id = id;
    this.grade = grade;
    this.evalDate = evalDate;
    this.report = report;
    this.attachments = attachments;
    this.evaluatedUser = evaluatedUser;
    this.subject = subject;
  }
}
