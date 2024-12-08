package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.Teacher;
import com.tuvarna.phd.exception.DoctoralCenterRoleNotFoundException;

public interface TeacherService {

  Teacher save(Teacher teacher) throws DoctoralCenterRoleNotFoundException;

  Teacher getTeacher(Teacher teacher) throws DoctoralCenterRoleNotFoundException;

  void delete(Teacher teacher);
}
