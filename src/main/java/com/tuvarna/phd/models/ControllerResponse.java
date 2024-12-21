package com.tuvarna.phd.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControllerResponse {

  private String message;
  private String role;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object data;

  public ControllerResponse(String message, Object data) {
    this.message = message;
    this.data = data;
  }

  public ControllerResponse(String message, String role, Object data) {
    this.message = message;
    this.role = role;
    this.data = data;
  }

  public ControllerResponse(String message) {
    this.message = message;
  }
}
