package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.exception.DoctoralCenterException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.util.List;

@ApplicationScoped
@Named("doctoralCenter")
public final class DoctoralCenterRepository extends SharedUserRepository
    implements PanacheRepositoryBase<DoctoralCenter, Integer>,
        UserRepositoryStrategy<DoctoralCenter> {

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
  public DoctoralCenter getFullByOid(String oid) {
    DoctoralCenter doctoralCenter = this.getByOid(oid);
    doctoralCenter.setPictureBlob(
        doctoralCenter.getPicture().isEmpty()
            ? ""
            : super.getDataUrlPicture(oid, doctoralCenter.getPicture()));

    return doctoralCenter;
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
}
