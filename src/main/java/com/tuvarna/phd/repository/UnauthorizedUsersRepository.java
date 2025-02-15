package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class UnauthorizedUsersRepository
    implements PanacheRepositoryBase<UnauthorizedUsers, Integer>,
        IUserRepository<UnauthorizedUsers> {

  @Override
  public UnauthorizedUsers getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new UnauthorizedException("Unauthorived user with id: " + id + " doesn't exist!"));
  }

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
  public void deleteByOid(String oid) {
    delete("oid", oid);
  }

  @Override
  public List<UnauthorizedUsers> getAll() {
    return listAll();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }
}
