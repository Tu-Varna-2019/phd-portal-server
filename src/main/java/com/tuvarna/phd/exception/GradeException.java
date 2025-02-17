package com.tuvarna.phd.exception;

public final class GradeException extends HttpException {

  public GradeException(String message) {
    super(message, 400);
  }

  public GradeException(String message, Integer status) {
    super(message, status);
  }
}
