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

  public Curriculum getByName(String name) {
    return find("name", name)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Curriculum name is not found: " + name));
  }

  public Curriculum getByNameAndModeId(String name, Long modeId) {
    return find("name = ?1 and mode.id = ?2", name, modeId)
        .firstResultOptional()
        .orElseThrow(
            () -> new HttpException("Curriculum name is not found: " + name + " " + modeId));
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
