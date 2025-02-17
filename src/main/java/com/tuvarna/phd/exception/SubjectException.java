package com.tuvarna.phd.exception;

public final class SubjectException extends HttpException {

  public SubjectException(String message) {
    super(message, 400);
  }

  public SubjectException(String message, Integer status) {
    super(message, status);
  }
}
