package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.DoctoralCenterRoleException;
import com.tuvarna.phd.service.dto.DoctoralCenterDTO;
import com.tuvarna.phd.service.dto.DoctoralCenterPasswordChangeDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.regex.Pattern;

@ApplicationScoped
public class DoctoralCenterValidator {

  protected enum VALID_ROLES {
    expert,
    manager;
  }

  private static final Pattern passwordPattern =
      Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

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

  public void validatePassword(DoctoralCenterPasswordChangeDTO dCenterPasswordChangeDTO)
      throws DoctoralCenterRoleException {
    /**
     * Validate the password Should At least 8 characters long. Should contain at least one
     * uppercase letter Should contain at least one lowercase letter Should contain at least one
     * digit Should contain at least one special character (e.g., @, $, !, %, *, ?, &).
     */
    if (!passwordPattern.matcher(dCenterPasswordChangeDTO.getNewPassword()).matches())
      throw new DoctoralCenterRoleException("Password does not conform pattern!");
  }
}
