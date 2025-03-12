package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Unauthorized;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class UnauthorizedRepository
    implements PanacheRepositoryBase<Unauthorized, Integer>, IUserRepository<Unauthorized> {

  @Override
  public Unauthorized getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new UnauthorizedException("Unauthorived user with id: " + id + " doesn't exist!"));
  }

  @Override
  public Unauthorized getByOid(String oid) {
    return find("oid", oid).firstResultOptional().orElse(null);
  }

  @Override
  public Unauthorized getByEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .orElseThrow(
            () -> new UnauthorizedException("Phd user with email: " + email + " doesn't exist!"));
  }

  @Override
  public void save(Unauthorized users) {
    users.persist();
  }

  @Override
  public void deleteByOid(String oid) {
    delete("oid", oid);
  }

  @Override
  public List<Unauthorized> getAll() {
    return listAll();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }
}
