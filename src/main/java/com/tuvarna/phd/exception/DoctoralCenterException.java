package com.tuvarna.phd.exception;

public class DoctoralCenterException extends HttpException {

  public DoctoralCenterException(String message) {
    super(message, 400);
  }

  public DoctoralCenterException(String message, Integer status) {
    super(message, status);
  }
}
