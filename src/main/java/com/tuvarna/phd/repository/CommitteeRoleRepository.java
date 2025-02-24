package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.CommitteeRole;
import com.tuvarna.phd.exception.HttpException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommitteeRoleRepository implements PanacheRepositoryBase<CommitteeRole, Integer> {

  public CommitteeRole getByRole(String role) {
    return find("role", role)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Committee role: " + role + " doesn't exist!", 404));
  }
}
