package com.tuvarna.phd.controller;

import com.tuvarna.phd.dto.UnauthorizedDTO;
import com.tuvarna.phd.entity.IUserEntity;
import com.tuvarna.phd.service.AuthService;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
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
@Path("/auth")
@Tag(name = "Authentication endpoint", description = "Endpoint for serving authentication services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public final class AuthController extends BaseController {

  private AuthService authService;
  @Inject JsonWebToken jwt;
  @Inject Logger LOG = Logger.getLogger(AuthController.class);

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @POST
  @Operation(
      summary = "Verify the user is present in the database",
      description = "Verify the user is present in the database")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "user is present indeed!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnauthorizedDTO.class))),
        @APIResponse(
            responseCode = "401",
            description = "User is not present and therefore not allowed!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnauthorizedDTO.class))),
      })
  @Path("/login")
  public Response login() {
    String oid = jwt.getClaim("oid");
    String name = jwt.getClaim("name");
    String email = jwt.getName();

    LOG.info("Received a request to login with user oid: " + oid + " and email: " + email);

    Tuple2<IUserEntity<?>, String> loggedUser = this.authService.login(oid, name, email);
    IUserEntity<?> user = loggedUser.getItem1();
    String group = loggedUser.getItem2();

    return send("User logged in!", user, group);
  }
}
