package com.tuvarna.phd.exception;

public class PhdException extends HttpException {

  public PhdException(String message) {
    super(message, 404);
  }

  public PhdException(String message, Integer status) {
    super(message, status);
  }
}
