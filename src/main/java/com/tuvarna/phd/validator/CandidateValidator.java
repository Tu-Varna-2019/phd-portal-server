package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.HttpException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CandidateValidator {

  private enum VALID_STASUSES {
    // NOTE: Will I realaly need approved in the candidateStatus repo, since we'lll already
    // increment the examPrep to +1 ?
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

  public void validateExamStep(Integer examStep) {
    if (examStep < 1 && examStep > 3)
      throw new HttpException("Exam step: " + examStep + " is invalid! Valid steps are 1-3");
  }
  ;
}
