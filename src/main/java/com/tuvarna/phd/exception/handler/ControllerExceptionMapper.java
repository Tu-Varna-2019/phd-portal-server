package com.tuvarna.phd.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuvarna.phd.exception.TeacherNotFoundException;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
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
      case TeacherNotFoundException e ->
          Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();

      default ->
          Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .entity("Unexpected error occured!")
              .build();
    };
  }
}
