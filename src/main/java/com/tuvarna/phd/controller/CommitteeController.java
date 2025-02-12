package com.tuvarna.phd.controller;

import com.tuvarna.phd.service.CommitteeService;
import com.tuvarna.phd.service.dto.CommitteeDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

@RequestScoped
@Path("/teacher")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public final class CommitteeController extends BaseController {

  private final CommitteeService committeeService;

  @Inject
  public CommitteeController(CommitteeService committeeService) {
    this.committeeService = committeeService;
  }

  @POST
  @Operation(summary = "Login to the system", description = "Login as a committee in the system")
  @APIResponses(
      value =
          @APIResponse(
              responseCode = "200",
              description = "Success",
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = CommitteeDTO.class))))
  @Path("/create")
  public void login(CommitteeDTO committeeDTO) {}
}
