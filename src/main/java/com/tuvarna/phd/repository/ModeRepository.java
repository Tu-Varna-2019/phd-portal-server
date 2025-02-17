package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Mode;
import com.tuvarna.phd.exception.PhdException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModeRepository implements PanacheRepositoryBase<Mode, Integer> {

  public Mode getByMode(Mode mode) {
    return find("mode", mode)
        .firstResultOptional()
        .orElseThrow(() -> new PhdException("Mode: " + mode + " doesn't exist!"));
  }
}
