package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.LogException;
import com.tuvarna.phd.service.dto.LogDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogValidator {

  protected enum VALID_GROUPS {
    phd,
    committee,
    // doctoralCenter;
    manager,
    expert
  }

  public void validateGroupExists(LogDTO logDTO) throws LogException {
    String group = logDTO.getUserPrincipalDTO().getGroup();

    try {
      if (!"user".equals(group)) VALID_GROUPS.valueOf(group);
    } catch (IllegalArgumentException e) {
      throw new LogException("Group " + group + " doesn't exist!");
    }
    ;
  }
}
