package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Commission;
import com.tuvarna.phd.exception.HttpException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class CommissionRepository
    implements PanacheRepositoryBase<Commission, Integer>, RepositoryStrategy<Commission> {

  @Override
  public Commission getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Commission user with id: " + id + " doesn't exist!"));
  }

  public Commission getByName(String name) {
    return find("name", name)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Commission name: " + name + " doesn't exist!"));
  }

  @Override
  public void save(Commission commission) {
    commission.persist();
  }

  @Override
  public List<Commission> getAll() {
    return listAll();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }

  public void deleteByName(String name) {
    delete("name", name);
  }
}
