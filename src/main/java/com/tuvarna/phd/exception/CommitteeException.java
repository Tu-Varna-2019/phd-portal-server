package com.tuvarna.phd.exception;

public class CommitteeException extends HttpException {

  public CommitteeException(String message) {
    super(message, 404);
  }

  public CommitteeException(String message, Integer status) {
    super(message, status);
  }
}
