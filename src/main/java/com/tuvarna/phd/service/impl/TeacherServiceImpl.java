package com.tuvarna.phd.service.impl;

import com.tuvarna.phd.entity.Teacher;
import com.tuvarna.phd.exception.TeacherNotFoundException;
import com.tuvarna.phd.repository.TeacherRepository;
import com.tuvarna.phd.service.TeacherService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TeacherServiceImpl implements TeacherService {
  private final TeacherRepository teacherRepository;

  @Inject private Logger log;

  @Inject
  public TeacherServiceImpl(TeacherRepository teacherRepository) {
    this.teacherRepository = teacherRepository;
  }

  @Override
  @Transactional
  public Teacher save(Teacher teacher) throws TeacherNotFoundException {
    teacherRepository.persistAndFlush(teacher);
    log.info(
        "Teacher created with id: "
            + teacherRepository.getTeacherByName(teacher.getName()).getId());

    return teacher;
  }

  @Override
  @Transactional
  public Teacher getTeacher(Teacher teacher) throws TeacherNotFoundException {

    return teacherRepository.getTeacherByName(teacher.getName());
  }

  @Override
  @Transactional
  public void delete(Teacher teacher) {
    teacherRepository.delete(teacher);
  }
}
