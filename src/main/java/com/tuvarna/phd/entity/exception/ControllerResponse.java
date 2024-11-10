package com.tuvarna.phd.entity.exception;

import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ControllerResponse<T> {
  private T data;
  private Response.Status status;
  private String message;

  public ControllerResponse(Response.Status status, String message) {
    this.status = status;
    this.message = message;
  }
}
