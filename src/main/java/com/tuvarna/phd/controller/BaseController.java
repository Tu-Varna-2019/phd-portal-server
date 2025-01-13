package com.tuvarna.phd.controller;

import com.tuvarna.phd.models.ControllerResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

public abstract class BaseController {

  public Response send(String message, Object data) {
    return Response.ok().entity(new ControllerResponse(message, data)).build();
  }

  public Response send(String message, String role, Object data) {
    return Response.ok().entity(new ControllerResponse(message, role, data)).build();
  }

  public Response send(String message) {
    return Response.ok().entity(new ControllerResponse(message)).build();
  }

  public Response send(String message, Integer status) {
    return Response.ok().status(status).entity(new ControllerResponse(message)).build();
  }

  public ResponseBuilder builder(byte[] bytes, String message) {
    return Response.ok(bytes).entity(new ControllerResponse(message));
  }

  public ResponseBuilder builder(String message, String role, Object data) {
    return Response.ok().entity(new ControllerResponse(message, role, data));
  }
}
