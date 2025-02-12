package com.tuvarna.phd.exception;

public final class BlobException extends HttpException {

  public BlobException(String message) {
    super(message, 400);
  }

  public BlobException(String message, Integer status) {
    super(message, status);
  }
}
