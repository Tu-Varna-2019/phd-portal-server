package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.DoctoralCenter;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DoctoralCenterRepository implements PanacheRepositoryBase<DoctoralCenter, Integer> {

  public void save(DoctoralCenter doctoralCenter) {
    doctoralCenter.persist();
  }

}
