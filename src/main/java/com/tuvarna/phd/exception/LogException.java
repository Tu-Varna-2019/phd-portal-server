package com.tuvarna.phd.exception;

public final class LogException extends HttpException {

  public LogException(String message) {
    super(message, 400);
  }
}
