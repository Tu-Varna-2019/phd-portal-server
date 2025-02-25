package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.exception.HttpException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class PhdRepository
    implements PanacheRepositoryBase<Phd, Integer>, IUserRepository<Phd> {

  @Override
  public Phd getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Phd user with id: " + id + " doesn't exist!"));
  }

  @Override
  public Phd getByOid(String oid) {
    return find("oid", oid)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Phd user with oid: " + oid + " doesn't exist!"));
  }

  @Override
  public Phd getByEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Phd user with email: " + email + " doesn't exist!"));
  }

  @Override
  public void save(Phd phd) {
    phd.persist();
  }

  @Override
  public void deleteByOid(String oid) {
    delete("oid", oid);
  }

  @Override
  public List<Phd> getAll() {
    return listAll();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }
}
