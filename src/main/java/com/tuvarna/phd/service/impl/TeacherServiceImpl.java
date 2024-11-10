package com.tuvarna.phd.service.impl;

import com.tuvarna.phd.entity.Teacher;
import com.tuvarna.phd.exception.TeacherNotFoundException;
import com.tuvarna.phd.repository.TeacherRepository;
import com.tuvarna.phd.service.TeacherService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TeacherServiceImpl implements TeacherService {
  private final TeacherRepository teacherRepository;

  @Inject
  public TeacherServiceImpl(TeacherRepository teacherRepository) {
    this.teacherRepository = teacherRepository;
  }

  @Override
  @Transactional
  public Teacher getTeacherById(long id) throws TeacherNotFoundException {
    return teacherRepository
        .findByIdOptional(id)
        .orElseThrow(() -> new TeacherNotFoundException("User doesn't exist!"));
  }

  @Override
  @Transactional
  public Teacher save(Teacher teacher) {
    teacherRepository.persistAndFlush(teacher);
    return teacher;
  }

  @Override
  @Transactional
  public void delete(Teacher teacher) {
    teacherRepository.delete(teacher);
  }
}
