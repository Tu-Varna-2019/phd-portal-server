package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Grade;
import com.tuvarna.phd.exception.GradeException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class GradeRepository
    implements PanacheRepositoryBase<Grade, Integer>, RepositoryStrategy<Grade> {
  @Override
  public Grade getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(() -> new GradeException("Grade not found with id: " + id));
  }

  @Override
  public void save(Grade grade) {
    grade.persist();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }

  @Override
  public List<Grade> getAll() {
    return listAll();
  }
}
