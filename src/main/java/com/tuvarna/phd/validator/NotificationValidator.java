package com.tuvarna.phd.validator;

import com.tuvarna.phd.dto.NotificationDTO;
import com.tuvarna.phd.exception.HttpException;

public class NotificationValidator {

  private enum VALID_SEVERITY {
    info,
    success,
    error,
    warn
  }

  private enum VALID_SCOPES {
    single,
    group
  }

  public void validateScopesExists(NotificationDTO notificationDTO) throws HttpException {
    try {
      VALID_SCOPES scope = VALID_SCOPES.valueOf(notificationDTO.getScope());

      if (scope.equals(VALID_SCOPES.single) && notificationDTO.getRecipients() == null)
        throw new HttpException("Single scope cannot be with emty recipients!");
      else if (scope.equals(VALID_SCOPES.group) && notificationDTO.getGroup() == null)
        throw new HttpException("Group scope cannot have empty group key!");

    } catch (IllegalArgumentException e) {
      throw new HttpException("Scope " + notificationDTO.getScope() + " doesn't exist!");
    }
    ;
  }

  public void validateSeverityExists(NotificationDTO notificationDTO) throws HttpException {
    try {
      VALID_SEVERITY.valueOf(notificationDTO.getSeverity());
    } catch (IllegalArgumentException e) {
      throw new HttpException("Severity " + notificationDTO.getSeverity() + " doesn't exist!");
    }
    ;
  }
}
