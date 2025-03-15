package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Subject;
import com.tuvarna.phd.exception.HttpException;
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
        .orElseThrow(() -> new HttpException("Subject not found with id: " + id));
  }

  public Subject getByName(String name) {
    return find("name", name)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Subject name is not found: " + name));
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
