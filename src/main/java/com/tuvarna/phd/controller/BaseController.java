package com.tuvarna.phd.controller;

import com.tuvarna.phd.models.ControllerResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

public abstract sealed class BaseController
    permits DoctoralCenterController,
        PhdController,
        AuthController,
        FileController,
        CandidateController,
        CommitteeController,
        DoctoralCenterAdminController,
        LogController,
        NotificationController {

  public Response send(String message, Object data) {
    return Response.ok().entity(new ControllerResponse(message, data)).build();
  }

  public Response send(String message, Object data, String group) {
    return Response.ok().entity(new ControllerResponse(message, data, group)).build();
  }

  public Response send(String message) {
    return Response.ok().entity(new ControllerResponse(message)).build();
  }

  public Response send(String message, Integer status) {
    return Response.ok().status(status).entity(new ControllerResponse(message)).build();
  }

  public Response send(String message, Object data, Integer status) {
    return Response.ok().status(status).entity(new ControllerResponse(message, data)).build();
  }

  public ResponseBuilder builder(byte[] bytes) {
    return Response.ok(bytes);
  }
}
