package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.HttpException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommitteeValidator {
  private enum VALID_EVAL_USER_TYPES {
    candidate,
    phd
  }

  public void validateEvalUserType(String type) {
    try {
      VALID_EVAL_USER_TYPES.valueOf(type);
    } catch (IllegalArgumentException e) {
      throw new HttpException(
          "Evaluated user type: "
              + type
              + " doesn't exist!/n Valid types: "
              + VALID_EVAL_USER_TYPES.values());
    }
  }
}
