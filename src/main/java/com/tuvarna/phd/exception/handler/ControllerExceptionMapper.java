package com.tuvarna.phd.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuvarna.phd.exception.PhdNotFoundException;
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

    return switch (exception) {
      case PhdNotFoundException e ->
          Response.status(404).entity(exception.getMessage()).build();

      case ForbiddenException e -> Response.status(403).entity("Forbidden!").build();
      case UnauthorizedException e -> Response.status(401).entity("Unauthorized!").build();

      default -> Response.status(500).entity("Unexpected error occured!").build();
    };
  }
}
