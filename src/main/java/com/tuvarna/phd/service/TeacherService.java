package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.Teacher;
import com.tuvarna.phd.exception.DoctoralCenterException;

public interface TeacherService {

  Teacher save(Teacher teacher) throws DoctoralCenterException;

  Teacher getTeacher(Teacher teacher) throws DoctoralCenterException;

  void delete(Teacher teacher);
}
