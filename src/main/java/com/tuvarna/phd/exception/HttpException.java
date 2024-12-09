package com.tuvarna.phd.exception;

import lombok.Getter;

@Getter
public abstract class HttpException extends RuntimeException {
  private final int status;

  protected HttpException(String message, int status) {
    super(message);
    this.status = status;
  }
}
