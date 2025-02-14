package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.exception.DoctoralCenterException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class DoctoralCenterRepository
    implements PanacheRepositoryBase<DoctoralCenter, Integer>, IUserRepository<DoctoralCenter> {

  @Override
  public DoctoralCenter getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new DoctoralCenterException(
                    "DoctoralCenter user with id: " + id + " doesn't exist!"));
  }

  @Override
  public void save(DoctoralCenter doctoralCenter) {
    doctoralCenter.persist();
  }

  @Override
  public DoctoralCenter getByOid(String oid) {
    return find("oid", oid)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new DoctoralCenterException(
                    "DoctoralCenter user with oid: " + oid + " doesn't exist!", 404));
  }

  @Override
  public void deleteByOid(String oid) {
    delete("oid", oid);
  }

  @Override
  public DoctoralCenter getByEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new DoctoralCenterException(
                    "DoctoralCenter user with email: " + email + " doesn't exist!", 404));
  }

  @Override
  public List<DoctoralCenter> getAll() {
    return listAll();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }
}
