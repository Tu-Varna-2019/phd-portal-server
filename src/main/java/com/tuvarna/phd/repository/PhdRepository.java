package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.exception.PhdException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class PhdRepository extends SharedUserRepository
    implements PanacheRepositoryBase<Phd, Integer>, UserRepositoryStrategy<Phd> {

  @Override
  public Phd getByOid(String oid) {
    return find("oid", oid)
        .firstResultOptional()
        .orElseThrow(() -> new PhdException("Phd user with oid: " + oid + " doesn't exist!"));
  }

  @Override
  public Phd getFullByOid(String oid) {
    Phd phd = this.getByOid(oid);
    phd.setPictureBlob(
        phd.getPicture().isEmpty() ? "" : super.getDataUrlPicture(oid, phd.getPicture()));

    return phd;
  }

  @Override
  public Phd getByEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .orElseThrow(() -> new PhdException("Phd user with email: " + email + " doesn't exist!"));
  }

  @Override
  public void save(Phd phd) {
    phd.persist();
  }

  @Override
  public void deleteByOid(String oid) {
    delete("oid", oid);
  }

  @Override
  public List<Phd> getAll() {
    return listAll();
  }
}
