package com.tuvarna.phd.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.models.ControllerResponse;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
@Priority(1)
public class ControllerExceptionMapper implements ExceptionMapper<Exception> {

  @Inject ObjectMapper objectMapper;
  @Inject private Logger log;

  @Override
  public Response toResponse(Exception exception) {
    Response response = mapExceptionToResponse(exception);
    log.error(
        "Error with exception: "
            + exception.getClass()
            + " with message: "
            + exception.getMessage());

    return Response.fromResponse(response).type(MediaType.APPLICATION_JSON).build();
  }

  private Response mapExceptionToResponse(Exception exception) {
    ControllerResponse controllerResponse = new ControllerResponse(exception.getMessage());
    return switch (exception) {
      case HttpException e -> Response.status(e.getStatus()).entity(controllerResponse).build();

      case ForbiddenException e -> {
        controllerResponse.setMessage("You are not permitted to do this operation");
        yield Response.status(403).entity(controllerResponse).build();
      }

      case UnauthorizedException e -> {
        controllerResponse.setMessage("Unauthorized!");
        yield Response.status(401).entity(controllerResponse).build();
      }

      default -> {
        controllerResponse.setMessage("Unexpected error occured. Try again at later time!");
        yield Response.status(500).entity(controllerResponse).build();
      }
    };
  }
}
