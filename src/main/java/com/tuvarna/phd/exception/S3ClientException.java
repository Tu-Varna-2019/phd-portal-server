package com.tuvarna.phd.exception;

public final class S3ClientException extends HttpException {

  public S3ClientException(String message) {
    super(message, 400);
  }
}
