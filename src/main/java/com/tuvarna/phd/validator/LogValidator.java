package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.HttpException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogValidator {

  private enum VALID_GROUPS {
    phd,
    committee,
    admin,
    manager,
    expert
  }

  private enum VALID_LEVEL {
    info,
    success,
    error,
    warn
  }

  public void validateGroupExists(String group) throws HttpException {
    try {
      if (!"user".equals(group)) VALID_GROUPS.valueOf(group);
    } catch (IllegalArgumentException e) {
      throw new HttpException("Group " + group + " doesn't exist!");
    }
    ;
  }

  public void isRoleAdmin(String role) throws HttpException {
    if (!role.equals("admin"))
      throw new HttpException(
          "Fetching logs for role: " + role + " is not permitted! Only admin is allowed.");
  }

  public void validateLevel(String level) throws HttpException {
    try {
      VALID_LEVEL.valueOf(level);
    } catch (IllegalArgumentException e) {
      throw new HttpException("Level " + level + " is invalid");
    }
  }
}
