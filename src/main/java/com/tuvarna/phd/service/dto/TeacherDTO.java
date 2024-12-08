package com.tuvarna.phd.service.dto;

import com.tuvarna.phd.entity.Teacher;
import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "TeacherDTO", description = "Teacher representation to create")
@Data
public class TeacherDTO {

  @NotNull
  @Schema(title = "name", required = true)
  private String name;

  public Teacher toTeacher() {
    Teacher teacher = new Teacher();
    teacher.setName(this.name);

    return teacher;
  }
}
