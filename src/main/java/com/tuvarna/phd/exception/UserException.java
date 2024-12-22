package com.tuvarna.phd.exception;

public class UserException extends HttpException {

  public UserException(String message) {
    super(message, 400);
  }

  public UserException(String message, Integer status) {
    super(message, status);
  }
}
