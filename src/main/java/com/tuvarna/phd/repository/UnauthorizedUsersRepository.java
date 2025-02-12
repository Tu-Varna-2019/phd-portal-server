package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UnauthorizedUsersRepository
    implements PanacheRepositoryBase<UnauthorizedUsers, Integer>,
        UserRepositoryStrategy<UnauthorizedUsers> {

  @Override
  public UnauthorizedUsers getByOid(String oid) {
    return find("oid", oid).firstResultOptional().orElse(null);
  }

  @Override
  public UnauthorizedUsers getByEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .orElseThrow(
            () -> new UnauthorizedException("Phd user with email: " + email + " doesn't exist!"));
  }

  @Override
  public void save(UnauthorizedUsers users) {
    users.persist();
  }

  @Override
  public UnauthorizedUsers getFullByOid(String oid) {
    throw new UnauthorizedException("Error: not implemented yet!");
  }

  @Override
  public void deleteByOid(String oid) {
    delete("oid", oid);
  }
}
