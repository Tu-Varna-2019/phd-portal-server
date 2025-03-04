package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Curriculum;
import com.tuvarna.phd.exception.HttpException;
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
        .orElseThrow(() -> new HttpException("Curriculum not found with id: " + id));
  }

  public Curriculum getByDescriptionAndModeId(String description, Long modeId) {
    return find("description = ?1 and mode.mode = ?2", description, modeId)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new HttpException(
                    "Curriculum description is not found: " + description + " " + modeId));
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
