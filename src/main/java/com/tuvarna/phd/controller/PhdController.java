package com.tuvarna.phd.controller;

import com.tuvarna.phd.exception.PhdNotFoundException;
import com.tuvarna.phd.service.PhdService;
import com.tuvarna.phd.service.dto.PhdDTO;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.jboss.logging.Logger;

@RequestScoped
@Path("/phd")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class PhdController extends BaseController {

  private final PhdService phdService;
  @Inject private static final Logger LOG = Logger.getLogger(DoctoralCenterController.class);

  @Inject SecurityIdentity identity;
  @Inject JsonWebToken jwt;

  @Inject
  public PhdController(PhdService phdService) {
    this.phdService = phdService;
  }

  @POST
  @Authenticated
  // @PermitAll
  @Transactional
  // @Authenticated
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
  // @HeaderParam("Authorization") String accessToken
  @Path("/login")
  public Response login(@Context SecurityContext ctx, PhdDTO pDto) throws PhdNotFoundException {
    LOG.info("Received a request to login from using Phd user creds: " + pDto);
    // this.phdService.login(pDto);
    // if (accessToken != null) LOG.info("Auth token: " + accessToken);

    Principal caller = ctx.getUserPrincipal();
    String name = caller == null ? "anonymous" : caller.getName();
    boolean hasJWT = jwt.getClaimNames() != null;
    LOG.info(
        String.format(
            "hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s",
            name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT));

    LOG.info("Phd user logged on!");

    return send("Phd user logged in!");
  }
}
