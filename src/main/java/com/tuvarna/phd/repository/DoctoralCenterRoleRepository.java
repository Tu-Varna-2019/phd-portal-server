package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.DoctoralCenterRole;
import com.tuvarna.phd.exception.DoctoralCenterRoleNotFoundException;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DoctoralCenterRoleRepository implements PanacheRepositoryBase<DoctoralCenterRole, Integer> {

  public DoctoralCenterRole getDoctoralCenterRoleById(Integer id) throws DoctoralCenterRoleNotFoundException {
    return findByIdOptional(id)
        .orElseThrow(() -> new DoctoralCenterRoleNotFoundException("Role for doctor center  doesn't exist!"));
  }

  public  DoctoralCenterRole getDoctoralCenterRoleByRole(String role) throws DoctoralCenterRoleNotFoundException {
    return find("role", role)
        .firstResultOptional()
        .orElseThrow(() -> new DoctoralCenterRoleNotFoundException("Role for doctor center  doesn't exist!"));
  }

}
