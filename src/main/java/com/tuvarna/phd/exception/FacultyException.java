package com.tuvarna.phd.exception;

public final class FacultyException extends HttpException {

  public FacultyException(String message) {
    super(message, 400);
  }

  public FacultyException(String message, Integer status) {
    super(message, status);
  }
}
