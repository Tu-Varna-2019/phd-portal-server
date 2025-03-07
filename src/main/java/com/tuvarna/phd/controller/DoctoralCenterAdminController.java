package com.tuvarna.phd.controller;

import com.tuvarna.phd.dto.OidDTO;
import com.tuvarna.phd.dto.RoleDTO;
import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.service.DoctoralCenterAdminService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestQuery;

@RequestScoped
@Path("/doctoralcenter/admin")
@Tag(
    name = "Admin doctoral center endpoint",
    description = "Endpoint for serving admin doctoral center services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public final class DoctoralCenterAdminController extends BaseController {
  @Inject private DoctoralCenterAdminService doctoralCenterAdminService;
  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterAdminController.class);

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
    List<UnauthorizedUsers> unauthorizedUsers =
        this.doctoralCenterAdminService.getUnauthorizedUsers();

    LOG.info("Unauthorized users received! Now sending to client...");
    return send("Unauthorized users retrieved!", unauthorizedUsers);
  }

  @POST
  @Operation(
      summary = "Set group for unauthorized users",
      description = "Set a group for unauthorized users")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Group set to unauthorized user",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnauthorizedUsersDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when setting a group for a unauthorized users!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnauthorizedUsersDTO.class))),
      })
  @Path("/unauthorized-users/group")
  public Response setRoleForUnauthorizedUsers(
      List<UnauthorizedUsersDTO> usersDTO, @RestQuery String group) {

    LOG.info("Received a request to set a group for unauthorized users: " + usersDTO.toString());
    this.doctoralCenterAdminService.setUnauthorizedUserGroup(usersDTO, group);

    return send("Unauthorized user is set for group: " + group);
  }

  @PATCH
  @Operation(
      summary = "Allow/Deny unauthorized users",
      description = "Allow/Deny unauthorized users")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Unauthorized users allowed/denied",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
        @APIResponse(
            responseCode = "400",
            description = " Error in setting allowed/denied for unauthorized users",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
      })
  @Path("/unauthorized-users/is-allowed")
  public Response seIsAllowedForUnauthorizedUsers(OidDTO oid, @RestQuery Boolean isAllowed) {

    LOG.info("Received a request to set isAllowed for unauthorized user oid: " + oid);
    this.doctoralCenterAdminService.changeUnauthorizedUserIsAllowed(oid.getOid(), isAllowed);

    return send("Unauthorized user changed isAllowed to: " + isAllowed);
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
  @Path("/authorized-users")
  public Response deleteAuthorizedUser(@RestQuery String oid, RoleDTO role) {
    LOG.info("Received a request to delete an auth user oid: " + oid + "for role: " + role);
    this.doctoralCenterAdminService.deleteAuthorizedUser(oid, role);

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
    List<UserDTO> authorizedUsers = this.doctoralCenterAdminService.getAuthorizedUsers();

    return send("authorized users retrieved!", authorizedUsers);
  }

  @GET
  @Operation(summary = "Doctoral Center roles", description = "Get all doctoral center roles")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "All doc center roles retrieved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving al doc center roles",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
      })
  @Path("/roles")
  public Response getDoctoralCenterRoles() {
    LOG.info("Received a request to get all doctoral center roles");
    List<String> docCenterRoles = this.doctoralCenterAdminService.getDoctoralCenterRoles();

    return send("All doctoral center roles retrieved!", docCenterRoles);
  }
}
