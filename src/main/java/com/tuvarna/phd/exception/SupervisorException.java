package com.tuvarna.phd.exception;

public final class SupervisorException extends HttpException {

  public SupervisorException(String message) {
    super(message, 400);
  }

  public SupervisorException(String message, Integer status) {
    super(message, status);
  }
}
