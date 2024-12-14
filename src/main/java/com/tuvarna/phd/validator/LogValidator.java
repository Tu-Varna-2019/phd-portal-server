package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.LogException;
import com.tuvarna.phd.service.dto.LogDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogValidator {

  public void validateRoleExists(LogDTO logDTO) throws LogException {
    String role = logDTO.getUserPrincipalDTO().getRole();

    try {
      if (!"user".equals(role)) DoctoralCenterValidator.VALID_ROLES.valueOf(role);
    } catch (IllegalArgumentException e) {
      throw new LogException("Role " + role + " doesn't exist!");
    }
    ;
  }
}
