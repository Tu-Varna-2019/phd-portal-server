package com.tuvarna.phd.controller;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.service.DoctoralCenterService;
import com.tuvarna.phd.service.dto.RoleDTO;
import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.service.dto.UserDTO;
import com.tuvarna.phd.validator.DoctoralCenterValidator;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.jboss.logging.Logger;

@RequestScoped
@Path("/doctoralcenter/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public class DoctoralCenterAdminController extends BaseController {

  private final DoctoralCenterService doctoralCenterService;
  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterAdminController.class);

  @Inject
  public DoctoralCenterAdminController(
      DoctoralCenterService doctoralCenterService,
      DoctoralCenterValidator doctoralCenterValidator) {
    this.doctoralCenterService = doctoralCenterService;
  }

  @GET
  @Operation(
      summary = "Unauthorized users",
      description = "Get all users, that attempted to sign in to the Phd platform")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "All unauthorized users retrieved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnauthorizedUsersDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving unauthorized users!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnauthorizedUsersDTO.class))),
      })
  @Path("/unauthorized-users")
  public Response getUnauthorizedUsers() {
    LOG.info("Received a request to get all unauthorized users");
    List<UnauthorizedUsers> unauthorizedUsers = this.doctoralCenterService.getUnauthorizedUsers();

    return send("Unauthorized users retrieved!", unauthorizedUsers);
  }

  @POST
  @Operation(
      summary = "Set role for unauthorized users",
      description = "Set a role for unauthorized users")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Role set to unauthorized user",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnauthorizedUsersDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when setting a role for a unauthorized users!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnauthorizedUsersDTO.class))),
      })
  @Path("/unauthorized-users/role/{role}")
  public Response setRoleForUnauthorizedUsers(List<UnauthorizedUsersDTO> usersDTO, String role) {
    LOG.info("Received a request to set a role for unauthorized users: " + usersDTO.toString());
    this.doctoralCenterService.setUnauthorizedUserRole(usersDTO, role);

    return send("Unauthorized user is set for role: " + role);
  }

  @DELETE
  @Operation(summary = "Delete a user", description = "Delete a user from the auth list")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "User deleted!",
            content = @Content(mediaType = "application/json")),
        @APIResponse(
            responseCode = "400",
            description = "Error when deleting user!",
            content = @Content(mediaType = "application/json")),
      })
  @Path("/authorized-users/{oid}")
  public Response deleteAuthorizedUser(String oid, RoleDTO role) {
    LOG.info("Received a request to delete an auth user oid: " + oid + "for role: " + role);
    this.doctoralCenterService.deleteAuthorizedUser(oid, role);

    return send("User: " + oid + " removed!");
  }

  @GET
  @Operation(summary = "User management", description = "Get all authorized users")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "All authorized users retrieved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving authtenticated users!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class))),
      })
  @Path("/authorized-users")
  public Response getAuthorizedUsers() {
    LOG.info("Received a request to get all authorized users");
    List<UserDTO> authorizedUsers = this.doctoralCenterService.getAuthorizedUsers();

    return send("authorized users retrieved!", authorizedUsers);
  }
}
