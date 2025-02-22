package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.DoctoralCenterRole;
import com.tuvarna.phd.exception.DoctoralCenterException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DoctoralCenterRoleRepository
    implements PanacheRepositoryBase<DoctoralCenterRole, Integer> {

  public DoctoralCenterRole getById(Integer id) {
    return findByIdOptional(id)
        .orElseThrow(() -> new DoctoralCenterException("Role for doctor center doesn't exist!"));
  }

  public DoctoralCenterRole getByRole(String role) {
    return find("role", role)
        .firstResultOptional()
        .orElseThrow(() -> new DoctoralCenterException("Role for doctor center doesn't exist!"));
  }
}
