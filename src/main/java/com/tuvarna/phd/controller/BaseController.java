package com.tuvarna.phd.controller;

import com.tuvarna.phd.models.ControllerResponse;
import jakarta.ws.rs.core.Response;

public abstract class BaseController {

  public Response send(String message, Object data) {
    return Response.ok().entity(new ControllerResponse(message, data)).build();
  }

  public Response send(String message) {
    return Response.ok().entity(new ControllerResponse(message)).build();
  }
}
