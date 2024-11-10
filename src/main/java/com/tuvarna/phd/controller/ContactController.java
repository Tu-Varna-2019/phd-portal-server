package com.tuvarna.phd.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

// import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
// import org.eclipse.microprofile.openapi.annotations.Operation;
// import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@RequestScoped
@Path("/contacts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
// @SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme =
// "basic")
public class ContactController {

  @GET
  @RolesAllowed({"USER", "ADMIN"})
  // @Operation(summary = "Gets users", description = "Lists all available users")
  // @APIResponses(value = @APIResponse(responseCode = "200", description = "Success",
  //                 content = @Content(mediaType = "application/json", schema =
  // @Schema(implementation = User.class))))
  public String getContacts() {
    return "Controllers";
  }
}
