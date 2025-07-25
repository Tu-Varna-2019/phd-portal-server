package com.tuvarna.phd.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuvarna.phd.controller.ControllerResponse;
import io.quarkus.security.UnauthorizedException;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
@Priority(1)
public class ControllerExceptionMapper implements ExceptionMapper<Exception> {

  @Inject ObjectMapper objectMapper;
  @Inject private Logger LOG = Logger.getLogger(ControllerExceptionMapper.class);

  @Override
  public Response toResponse(Exception exception) {
    Response response = mapExceptionToResponse(exception);
    LOG.error("Exception: " + exception.getClass() + " Message: " + exception.getMessage());

    return Response.fromResponse(response).type(MediaType.APPLICATION_JSON).build();
  }

  private Response mapExceptionToResponse(Exception exception) {
    return switch (exception) {
      case HttpException e -> {
        yield Response.status(e.getStatus()).entity(new ControllerResponse(e.getMessage())).build();
      }

      case ForbiddenException e -> {
        yield Response.status(403)
            .entity(
                new ControllerResponse(
                    e.getMessage().isEmpty()
                        ? "You are not permitted to do this operation"
                        : e.getMessage()))
            .build();
      }

      case UnauthorizedException e -> {
        yield Response.status(401)
            .entity(
                new ControllerResponse(e.getMessage().isEmpty() ? "Unauthorized!" : e.getMessage()))
            .build();
      }

      case BadRequestException e -> {
        yield Response.status(400).entity(new ControllerResponse(e.getMessage())).build();
      }

      default -> {
        yield Response.status(500)
            .entity(new ControllerResponse("Unexpected error occured. Try again at later time!"))
            .build();
      }
    };
  }
}
