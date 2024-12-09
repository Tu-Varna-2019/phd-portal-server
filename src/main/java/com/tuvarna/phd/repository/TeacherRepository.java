package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Teacher;
import com.tuvarna.phd.exception.DoctoralCenterException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TeacherRepository implements PanacheRepositoryBase<Teacher, Integer> {

  public Teacher getTeacherById(Integer id) throws DoctoralCenterException {
    return findByIdOptional(id)
        .orElseThrow(() -> new DoctoralCenterException("Teacher doesn't exist!"));
  }

  public Teacher getTeacherByName(String name) throws DoctoralCenterException {
    return find("name", name)
        .firstResultOptional()
        .orElseThrow(() -> new DoctoralCenterException("Teacher doesn't exist!"));
  }
}
