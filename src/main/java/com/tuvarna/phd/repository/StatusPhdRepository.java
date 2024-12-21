package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.StatusPhd;
import com.tuvarna.phd.exception.PhdException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StatusPhdRepository implements PanacheRepositoryBase<StatusPhd, Integer> {

  public StatusPhd getByStatus(String status) {
    return find("status", status)
        .firstResultOptional()
        .orElseThrow(() -> new PhdException("Phd with status: " + status + " doesn't exist!", 404));
  }
}
