package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.HttpException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class CommitteeValidator {

  private enum VALID_EVAL_USER_TYPES {
    candidate,
    phd
  }

  private List<Double> VALID_GRADES = Arrays.asList(2.00, 3.00, 4.00, 5.00, 6.00);

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

  public void validateGrade(Double grade) {
    if (!VALID_GRADES.contains(grade)) {
      throw new HttpException(
          "Grade: " + grade + "is not valid! Valid grades: " + VALID_GRADES.toString());
    }
  }
}
