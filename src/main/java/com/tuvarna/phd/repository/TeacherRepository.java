package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Teacher;
import com.tuvarna.phd.exception.DoctoralCenterRoleNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TeacherRepository implements PanacheRepositoryBase<Teacher, Integer> {

  public Teacher getTeacherById(Integer id) throws DoctoralCenterRoleNotFoundException {
    return findByIdOptional(id)
        .orElseThrow(() -> new DoctoralCenterRoleNotFoundException("Teacher doesn't exist!"));
  }

  public Teacher getTeacherByName(String name) throws DoctoralCenterRoleNotFoundException {
    return find("name", name)
        .firstResultOptional()
        .orElseThrow(() -> new DoctoralCenterRoleNotFoundException("Teacher doesn't exist!"));
  }
}
