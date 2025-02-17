package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Curriculum;
import com.tuvarna.phd.exception.CurriculumException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CurriculumRepository
    implements PanacheRepositoryBase<Curriculum, Integer>, RepositoryStrategy<Curriculum> {

  @Override
  public Curriculum getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(() -> new CurriculumException("Curriculum not found with id: " + id));
  }

  @Override
  public void save(Curriculum curriculum) {
    curriculum.persist();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }

  @Override
  public List<Curriculum> getAll() {
    return listAll();
  }
}
