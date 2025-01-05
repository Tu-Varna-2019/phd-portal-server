package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.exception.DoctoralCenterException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class DoctoralCenterRepository implements PanacheRepositoryBase<DoctoralCenter, Integer> {

  public void save(DoctoralCenter doctoralCenter) {
    doctoralCenter.persist();
  }

  public DoctoralCenter getDoctoralCenterByEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .orElseThrow(
            () -> new DoctoralCenterException("User with email: " + email + " doesn't exists!"));
  }

  public boolean existsByEmail(String email) {
    return count("email = ?1", email) > 0;
  }

  public DoctoralCenter getByOid(String oid) {
    return find("oid", oid)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new DoctoralCenterException(
                    "DoctoralCenter user with oid: " + oid + " doesn't exist!", 404));
  }

  public List<DoctoralCenter> getAll() {
    return listAll();
  }

  public void deleteByOid(String oid) {
    delete("oid", oid);
  }
}
