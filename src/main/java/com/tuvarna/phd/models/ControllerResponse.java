package com.tuvarna.phd.models;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class ControllerResponse {
  private Response response;

  public ControllerResponse(Integer status, Object data) {
    this.response = Response.status(status).entity(data).build();
  }
}
