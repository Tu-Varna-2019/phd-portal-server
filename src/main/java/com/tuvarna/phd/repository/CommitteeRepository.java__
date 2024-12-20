package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.exception.CommitteeException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommitteeRepository implements PanacheRepositoryBase<Committee, Integer> {

  public Committee getCommitteeById(Integer id) throws CommitteeException {
    return findByIdOptional(id)
        .orElseThrow(() -> new CommitteeException("Committee doesn't exist!", 404));
  }

  public Committee getByOid(String oid) {
    return find("oid", oid)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new CommitteeException("Committee user with oid: " + oid + " doesn't exist!", 404));
  }

  public void save(Committee committee) {
    committee.persist();
  }
}
