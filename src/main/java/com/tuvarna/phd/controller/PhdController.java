package com.tuvarna.phd.controller;

import com.tuvarna.phd.dto.PhdDTO;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.service.PhdService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

@RequestScoped
@Path("/phd")
@Tag(name = "Phd endpoint", description = "Endpoint for serving phd services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public final class PhdController extends BaseController {

  private PhdService phdService;
  @Inject Logger LOG = Logger.getLogger(PhdController.class);

  @Inject
  public PhdController(PhdService phdService) {
    this.phdService = phdService;
  }

  @POST
  @Operation(
      summary = "Login to the Phd portal",
      description =
          "Login to the PHD portal and synchronize the AAD (Entra ID) user with Postgre's")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Phd user logged in!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PhdDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when logging in via Phd user",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PhdDTO.class))),
      })
  @Path("/login")
  public Response login(PhdDTO pDto) throws HttpException {
    // LOG.info("Received a request to login from using Phd user creds: " + pDto);
    // this.phdService.login(pDto);

    LOG.info("Phd user logged on!");

    return send("Phd user logged in!");
  }
}
