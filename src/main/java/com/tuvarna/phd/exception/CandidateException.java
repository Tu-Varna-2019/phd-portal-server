package com.tuvarna.phd.exception;

public final class CandidateException extends HttpException {

  public CandidateException(String message) {
    super(message, 400);
  }

  public CandidateException(String message, Integer status) {
    super(message, status);
  }
}
