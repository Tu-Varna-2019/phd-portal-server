package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Faculty;
import com.tuvarna.phd.exception.FacultyException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FacultyRepository implements PanacheRepositoryBase<Faculty, Integer> {

  public Faculty getByname(String name) {
    return find("name", name)
        .firstResultOptional()
        .orElseThrow(() -> new FacultyException("Faculty not found with name: " + name));
  }
}
