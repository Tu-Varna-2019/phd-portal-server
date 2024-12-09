package com.tuvarna.phd.exception;


public class PhdNotFoundException extends HttpException {

  public PhdNotFoundException(String message) {
    super(message,404);
  }
}
