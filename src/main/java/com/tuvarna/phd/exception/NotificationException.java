package com.tuvarna.phd.exception;

public final class NotificationException extends HttpException {

  public NotificationException(String message) {
    super(message, 404);
  }

  public NotificationException(String message, Integer status) {
    super(message, status);
  }
}
