package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.Teacher;
import com.tuvarna.phd.exception.TeacherNotFoundException;

public interface TeacherService {

  // Teacher findbyName(Teacher teacher);

  Teacher save(Teacher teacher);

  Teacher getTeacherById(long id) throws TeacherNotFoundException;

  void delete(Teacher teacher);
}
