package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.exception.CommitteeException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class CommitteeRepository extends SharedUserRepository
    implements PanacheRepositoryBase<Committee, Integer> {

  public Committee getCommitteeById(Integer id) throws CommitteeException {
    return findByIdOptional(id)
        .orElseThrow(() -> new CommitteeException("Committee doesn't exist!", 404));
  }

  public Committee getByOid(String oid) {
    return find("oid", oid)
        .firstResultOptional()
        .orElseThrow(
            () ->
                new CommitteeException("Committee user with oid: " + oid + " doesn't exist!", 404));
  }

  public Committee getFullByOid(String oid) {
    Committee committee = this.getByOid(oid);
    committee.setPictureBlob(super.getDataUrlPicture(oid, committee.getPicture()));

    return committee;
  }

  public void save(Committee committee) {
    committee.persist();
  }

  public List<Committee> getAll() {
    return listAll();
  }

  public void deleteByOid(String oid) {
    delete("oid", oid);
  }
}
