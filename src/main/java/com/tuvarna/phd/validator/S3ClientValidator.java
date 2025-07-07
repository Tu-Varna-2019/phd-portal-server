package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.HttpException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class S3ClientValidator {

  private enum VALID_TYPES {
    avatar,
    biography,
    reports,
    grades
  }

  public void validateType(String type) throws HttpException {
    try {
      VALID_TYPES.valueOf(type);
    } catch (IllegalArgumentException e) {
      throw new HttpException(
          "File type " + type + " is invalid!\n Valid types: " + VALID_TYPES.values());
    }
  }
}
