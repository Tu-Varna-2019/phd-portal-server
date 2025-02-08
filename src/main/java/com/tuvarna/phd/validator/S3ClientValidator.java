package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.S3ClientException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class S3ClientValidator {

  private enum VALID_TYPES {
    avatar,
    docs
  }

  public void validateType(String type) throws S3ClientException {
    try {
      VALID_TYPES.valueOf(type);
    } catch (IllegalArgumentException e) {
      throw new S3ClientException(
          "File type " + type + " is invalid!\n Valid types: " + VALID_TYPES.values());
    }
  }
}
