package com.tuvarna.phd.exception;

public final class IPBlockException extends HttpException {

  public IPBlockException(String message) {
    super(message, 400);
  }

  public IPBlockException(String message, Integer status) {
    super(message, status);
  }
}
