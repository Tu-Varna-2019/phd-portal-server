package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.DoctoralCenterRole;
import com.tuvarna.phd.exception.HttpException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DoctoralCenterRoleRepository
    implements PanacheRepositoryBase<DoctoralCenterRole, Integer> {

  public DoctoralCenterRole getById(Integer id) {
    return findByIdOptional(id)
        .orElseThrow(() -> new HttpException("Role for doctor center doesn't exist!"));
  }

  public DoctoralCenterRole getByRole(String role) {
    return find("role", role)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Role for doctor center doesn't exist!"));
  }
}
