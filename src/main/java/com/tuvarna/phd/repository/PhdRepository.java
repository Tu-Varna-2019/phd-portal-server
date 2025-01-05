package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.exception.PhdException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PhdRepository implements PanacheRepositoryBase<Phd, Integer> {

  public Phd getById(Integer id) throws PhdException {
    return findByIdOptional(id).orElseThrow(() -> new PhdException("Phd doesn't exist!"));
  }

  public Phd getByOid(String oid) {
    return find("oid", oid)
        .firstResultOptional()
        .orElseThrow(() -> new PhdException("Phd user with oid: " + oid + " doesn't exist!"));
  }

  public Phd getByEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .orElseThrow(() -> new PhdException("Phd user with email: " + email + " doesn't exist!"));
  }

  public void save(Phd phd) {
    phd.persist();
  }

  public List<Phd> getAll() {
    return listAll();
  }

  public void deleteByOid(String oid) {
    delete("oid", oid);
  }
}
