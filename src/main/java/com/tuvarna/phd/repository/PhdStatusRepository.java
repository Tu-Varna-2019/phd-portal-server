package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.PhdStatus;
import com.tuvarna.phd.exception.HttpException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PhdStatusRepository implements PanacheRepositoryBase<PhdStatus, Integer> {

  public PhdStatus getByStatus(String status) {
    return find("status", status)
        .firstResultOptional()
        .orElseThrow(
            () -> new HttpException("Phd with status: " + status + " doesn't exist!", 404));
  }
}
