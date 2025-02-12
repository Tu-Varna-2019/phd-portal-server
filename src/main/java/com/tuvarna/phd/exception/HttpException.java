package com.tuvarna.phd.exception;

import lombok.Getter;

@Getter
public abstract sealed class HttpException extends RuntimeException
    permits BlobException,
        CandidateException,
        CommitteeException,
        DoctoralCenterException,
        DoctoralCenterRoleException,
        IPBlockException,
        LogException,
        NotificationException,
        PhdException,
        S3ClientException,
        UserException {
  private final int status;

  protected HttpException(String message, int status) {
    super(message);
    this.status = status;
  }
}
