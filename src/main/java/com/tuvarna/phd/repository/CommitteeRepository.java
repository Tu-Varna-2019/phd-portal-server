package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.exception.CommitteeException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class CommitteeRepository extends SharedUserRepository
    implements PanacheRepositoryBase<Committee, Integer>, UserRepositoryStrategy<Committee> {

  public Committee getCommitteeById(Integer id) throws CommitteeException {
    return findByIdOptional(id)
        .orElseThrow(() -> new CommitteeException("Committee doesn't exist!", 404));
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
  public Committee getFullByOid(String oid) {
    Committee committee = this.getByOid(oid);
    committee.setPictureBlob(
        committee.getPicture().isEmpty()
            ? ""
            : super.getDataUrlPicture(oid, committee.getPicture()));

    return committee;
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
}
