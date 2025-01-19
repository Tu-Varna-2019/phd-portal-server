package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.LogException;
import com.tuvarna.phd.service.dto.LogDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogValidator {

  private enum VALID_GROUPS {
    phd,
    committee,
    // doctoralCenter;
    manager,
    expert
  }

  private enum VALID_LEVEL {
    INFO,
    SUCCESS,
    ERROR,
    WARN
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

  public void validateLevel(LogDTO logDTO) throws LogException {
    String level = logDTO.getLevel();
    try {
      VALID_LEVEL.valueOf(level);
    } catch (IllegalArgumentException e) {
      throw new LogException("Level " + level + " is invalid");
    }
  }
}
