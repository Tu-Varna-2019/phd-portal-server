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

  public Subject getByCourse(Integer course) {
    return find("course", course)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Subject course is not found: " + course));
  }

  public Subject getBySemester(Integer semester) {
    return find("semester", semester)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Subject semester is not found: " + semester));
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
