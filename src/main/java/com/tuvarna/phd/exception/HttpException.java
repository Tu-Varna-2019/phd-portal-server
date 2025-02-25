package com.tuvarna.phd.exception;

import lombok.Getter;

@Getter
public class HttpException extends RuntimeException {
  private final int status;

  public HttpException(String message, int status) {
    super(message);
    this.status = status;
  }

  public HttpException(String message) {
    super(message);
    this.status = 400;
  }
}
