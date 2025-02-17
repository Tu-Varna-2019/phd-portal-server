package com.tuvarna.phd.exception;

public final class CurriculumException extends HttpException {

  public CurriculumException(String message) {
    super(message, 400);
  }

  public CurriculumException(String message, Integer status) {
    super(message, status);
  }
}
