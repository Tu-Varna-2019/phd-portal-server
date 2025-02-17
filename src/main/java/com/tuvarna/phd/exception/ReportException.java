package com.tuvarna.phd.exception;

public final class ReportException extends HttpException {

  public ReportException(String message) {
    super(message, 400);
  }

  public ReportException(String message, Integer status) {
    super(message, status);
  }
}
