package com.tuvarna.phd.validator;

import com.tuvarna.phd.dto.DoctoralCenterDTO;
import com.tuvarna.phd.exception.HttpException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DoctoralCenterValidator {

  protected enum VALID_ROLES {
    expert,
    manager;
  }

  public void validateRoleIsExpert(DoctoralCenterDTO doctoralCenterDTO) throws HttpException {
    try {
      VALID_ROLES isRoleExpert = VALID_ROLES.valueOf(doctoralCenterDTO.getRole());
      if (isRoleExpert != VALID_ROLES.expert) throw new HttpException("Role is not expert!");
    } catch (IllegalArgumentException e) {
      throw new HttpException("Role is not valid!");
    }
    ;
  }

  public void validateRole(DoctoralCenterDTO doctoralCenterDTO) throws HttpException {
    try {
      VALID_ROLES.valueOf(doctoralCenterDTO.getRole());
    } catch (IllegalArgumentException e) {
      throw new HttpException("Role is not valid!");
    }
    ;
  }

  public void validateRole(String role) throws HttpException {
    try {
      VALID_ROLES.valueOf(role);
    } catch (IllegalArgumentException e) {
      throw new HttpException("Role is not valid!");
    }
    ;
  }
}
