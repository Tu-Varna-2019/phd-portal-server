package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.DoctoralCenterRoleException;
import com.tuvarna.phd.service.dto.DoctoralCenterDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DoctoralCenterValidator {

  protected enum VALID_ROLES {
    expert,
    manager;
  }

  public void validateRoleIsExpert(DoctoralCenterDTO doctoralCenterDTO)
      throws DoctoralCenterRoleException {
    try {
      VALID_ROLES isRoleExpert = VALID_ROLES.valueOf(doctoralCenterDTO.getRole());
      if (isRoleExpert != VALID_ROLES.expert)
        throw new DoctoralCenterRoleException("Role is not expert!");
    } catch (IllegalArgumentException e) {
      throw new DoctoralCenterRoleException("Role is not valid!");
    }
    ;
  }

  public void validateRole(DoctoralCenterDTO doctoralCenterDTO) throws DoctoralCenterRoleException {
    try {
      VALID_ROLES.valueOf(doctoralCenterDTO.getRole());
    } catch (IllegalArgumentException e) {
      throw new DoctoralCenterRoleException("Role is not valid!");
    }
    ;
  }

  public void validateRole(String role) throws DoctoralCenterRoleException {
    try {
      VALID_ROLES.valueOf(role);
    } catch (IllegalArgumentException e) {
      throw new DoctoralCenterRoleException("Role is not valid!");
    }
    ;
  }
}
