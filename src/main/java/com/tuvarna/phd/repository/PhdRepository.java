package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.exception.PhdException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PhdRepository implements PanacheRepositoryBase<Phd, Integer> {

  public Phd getPhdById(Integer id) throws PhdException {
    return findByIdOptional(id).orElseThrow(() -> new PhdException("Phd doesn't exist!"));
  }

  public Phd getByOid(String oid) {
    return find("oid", oid)
        .firstResultOptional()
        .orElseThrow(() -> new PhdException("Phd user with oid: " + oid + " doesn't exist!"));
  }

  public void save(Phd phd) {
    phd.persist();
  }
}
