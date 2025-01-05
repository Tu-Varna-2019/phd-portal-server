package com.tuvarna.phd.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ControllerResponse {

  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String group;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object data;

  public ControllerResponse(String message, Object data) {
    this.message = message;
    this.data = data;
  }

  public ControllerResponse(String message) {
    this.message = message;
  }
}
