package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.LogException;
import com.tuvarna.phd.service.dto.LogDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogValidator {

  public void validateRoleExists(LogDTO logDTO) throws LogException {
    try {
      if (!"user".equals(logDTO.getRole()))
        DoctoralCenterValidator.VALID_ROLES.valueOf(logDTO.getRole());
    } catch (IllegalArgumentException e) {
      throw new LogException("Role " + logDTO.getRole() + " doesn't exist!");
    }
    ;
  }
}
