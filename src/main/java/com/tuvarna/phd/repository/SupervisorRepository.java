package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Supervisor;
import com.tuvarna.phd.exception.SupervisorException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class SupervisorRepository
    implements PanacheRepositoryBase<Supervisor, Integer>, IUserRepository<Supervisor> {

  @Override
  public Supervisor getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(
            () -> new SupervisorException("Supervisor user with id: " + id + " doesn't exist!"));
  }

  @Override
  public Supervisor getByOid(String oid) {
    return find("oid", oid)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new SupervisorException(
                    "Supervisor user with oid: " + oid + " doesn't exist!", 404));
  }

  @Override
  public Supervisor getByEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new SupervisorException(
                    "Supervisor user with email: " + email + " doesn't exist!"));
  }

  @Override
  public void save(Supervisor committee) {
    committee.persist();
  }

  @Override
  public List<Supervisor> getAll() {
    return listAll();
  }

  @Override
  public void deleteByOid(String oid) {
    delete("oid", oid);
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }
}
