package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.exception.CommitteeException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class CommitteeRepository
    implements PanacheRepositoryBase<Committee, Integer>, IUserRepository<Committee> {

  @Override
  public Committee getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(
            () -> new CommitteeException("Committee user with id: " + id + " doesn't exist!"));
  }

  @Override
  public Committee getByOid(String oid) {
    return find("oid", oid)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new CommitteeException("Committee user with oid: " + oid + " doesn't exist!", 404));
  }

  @Override
  public Committee getByEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new CommitteeException("Committee user with email: " + email + " doesn't exist!"));
  }

  @Override
  public void save(Committee committee) {
    committee.persist();
  }

  @Override
  public List<Committee> getAll() {
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
