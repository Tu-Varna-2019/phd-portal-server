package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.HttpException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CandidateValidator {

  private enum VALID_STASUSES {
    approved,
    rejected
  }

  public void validateStatusExists(String status) {
    try {
      VALID_STASUSES.valueOf(status);
    } catch (IllegalArgumentException e) {
      throw new HttpException("Status " + status + " doesn't exist!");
    }
    ;
  }
}
