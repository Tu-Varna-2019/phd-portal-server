package com.tuvarna.phd.controller;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ControllerResponse(
    String message,
    @JsonInclude(JsonInclude.Include.NON_NULL) Object data,
    @JsonInclude(JsonInclude.Include.NON_NULL) String group) {
  public ControllerResponse(String message) {
    this(message, null, null);
  }

  public ControllerResponse(String message, Object data) {
    this(message, data, null);
  }
}
