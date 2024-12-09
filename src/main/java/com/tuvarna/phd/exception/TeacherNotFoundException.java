package com.tuvarna.phd.exception;


public class TeacherNotFoundException extends HttpException {

  public TeacherNotFoundException(String message) {
    super(message,404);
  }
}
