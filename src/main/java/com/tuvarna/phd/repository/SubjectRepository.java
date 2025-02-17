package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Subject;
import com.tuvarna.phd.exception.SubjectException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SubjectRepository
    implements PanacheRepositoryBase<Subject, Integer>, RepositoryStrategy<Subject> {

  @Override
  public Subject getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(() -> new SubjectException("Subject not found with id: " + id));
  }

  @Override
  public void save(Subject subject) {
    subject.persist();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }

  @Override
  public List<Subject> getAll() {
    return listAll();
  }
}
