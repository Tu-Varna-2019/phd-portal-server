package com.tuvarna.phd.exception;

public class LogException extends HttpException {

  public LogException(String message) {
    super(message, 400);
  }
}
