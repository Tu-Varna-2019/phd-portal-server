package com.tuvarna.phd.exception;

public final class DoctoralCenterRoleException extends HttpException {

  public DoctoralCenterRoleException(String message) {
    super(message, 404);
  }
}
