package com.tuvarna.phd.validator;

import com.tuvarna.phd.dto.NotificationDTO;
import com.tuvarna.phd.exception.NotificationException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
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

  public void validateScopesExists(NotificationDTO notificationDTO) throws NotificationException {
    try {
      VALID_SCOPES scope = VALID_SCOPES.valueOf(notificationDTO.getScope());
      System.out.println(notificationDTO.toString());

      if (scope.equals(VALID_SCOPES.single) && notificationDTO.getRecipients() == null)
        throw new NotificationException("Single scope cannot be with emty recipients!");
      else if (scope.equals(VALID_SCOPES.group) && notificationDTO.getGroup() == null)
        throw new NotificationException("Group scope cannot have empty group key!");

    } catch (IllegalArgumentException e) {
      throw new NotificationException("Scope " + notificationDTO.getScope() + " doesn't exist!");
    }
    ;
  }

  public void validateSeverityExists(NotificationDTO notificationDTO) throws NotificationException {
    try {
      VALID_SEVERITY.valueOf(notificationDTO.getSeverity());
    } catch (IllegalArgumentException e) {
      throw new NotificationException(
          "Severity " + notificationDTO.getSeverity() + " doesn't exist!");
    }
    ;
  }
}
