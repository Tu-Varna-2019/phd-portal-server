package com.tuvarna.phd.service.dto;

import com.sun.istack.NotNull;
import com.tuvarna.phd.entity.Teacher;
import lombok.Data;

// import org.eclipse.microprofile.openapi.annotations.media.Schema;

// @Schema(name="UserDTO", description="User representation to create")
@Data
public class TeacherDTO {

  // @NotBlank
  // @Schema(title = "Username", required = true)
  //
  // @Min(value = 1, message = "The value must be more than 0")
  // @Max(value = 200, message = "The value must be less than 200")
  @NotNull private String name;

  public Teacher toTeacher() {
    Teacher teacher = new Teacher();
    teacher.setName(this.name);
    // teacher.setName(this.name);

    return teacher;
  }
}
