package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.Teacher;
import com.tuvarna.phd.exception.TeacherNotFoundException;

public interface TeacherService {

  Teacher save(Teacher teacher) throws TeacherNotFoundException;

  void delete(Teacher teacher);
}
