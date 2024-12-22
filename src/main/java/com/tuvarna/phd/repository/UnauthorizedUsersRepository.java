package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UnauthorizedUsersRepository
    implements PanacheRepositoryBase<UnauthorizedUsers, Integer> {

  public UnauthorizedUsers getByOid(String oid) {
    return find("oid", oid).firstResultOptional().orElse(null);
  }

  public void save(UnauthorizedUsers users) {
    users.persist();
  }
}
