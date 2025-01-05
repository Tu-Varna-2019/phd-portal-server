package com.tuvarna.phd.controller;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.service.DoctoralCenterService;
import com.tuvarna.phd.service.dto.PhdDTO;
import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.service.dto.UserDTO;
import com.tuvarna.phd.validator.DoctoralCenterValidator;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
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
import org.jboss.logging.Logger;

@RequestScoped
@Path("/doctoralcenter")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class DoctoralCenterController extends BaseController {

  private final DoctoralCenterService doctoralCenterService;
  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterController.class);

  @Inject
  public DoctoralCenterController(
      DoctoralCenterService doctoralCenterService,
      DoctoralCenterValidator doctoralCenterValidator) {
    this.doctoralCenterService = doctoralCenterService;
  }

  @PATCH
  @Operation(summary = "Phd's candidate", description = "Approve or reject phd's candidate")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Phd approved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PhdDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when approving/rejecting phd's candidate",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PhdDTO.class))),
      })
  @Path("/candidates/{status}")
  public Uni<Response> updateStatusCandidate(PhdDTO pDto, String status) {
    LOG.info("Received a request to " + status + "a phd user: " + pDto.getEmail());

    this.doctoralCenterService.updatePhdStatus(pDto, status);

    if (status.equals("approved")) {

      LOG.info("Email is being sent for the phd user: " + pDto.getEmail());
      Uni<Void> emailSent = this.doctoralCenterService.sendEmail(pDto.getEmail());

      return emailSent
          .onItem()
          .transform(
              v -> {
                return send("Phd user email sent!");
              });
    }
    return Uni.createFrom().item(send("Phd user status changed to: " + status));
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
  @Path("/unauthorized/get")
  public Response getUnauthorizedUsers(
      @NotBlank @Pattern(regexp = "^doctoralCenter") @CookieParam("group") String group,
      @NotBlank @Pattern(regexp = "^admin") @CookieParam("role") String role) {
    LOG.info("Received a request to get all unauthorized users");

    List<UnauthorizedUsers> unauthorizedUsers = this.doctoralCenterService.getUnauthorizedUsers();

    return send("Unauthorized users retrieved!", unauthorizedUsers);
  }

  @GET
  @Operation(summary = "User management", description = "Get all authenticated users")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "All authenticated users retrieved",
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
  @Path("/authenticated/get")
  public Response getAuthenticatedUsers(
      @NotBlank @Pattern(regexp = "^doctoralCenter") @CookieParam("group") String group,
      @NotBlank @Pattern(regexp = "^admin") @CookieParam("role") String role) {
    LOG.info("Received a request to get all authtenticated users");

    List<UserDTO> authenticatedUsers = this.doctoralCenterService.getAuthenticatedUsers();

    return send("Authenticated users retrieved!", authenticatedUsers);
  }

  @POST
  @Operation(
      summary = "Set role for unauthorized user",
      description = "Set a role for unauthorized user")
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
  @Path("/unauthorized/set/role/{role}")
  public Response setRoleForUnauthorizedUser(
      @NotBlank @Pattern(regexp = "^doctoralCenter") @CookieParam("group") String group,
      @NotBlank @Pattern(regexp = "^admin") @CookieParam("role") String roleCookie,
      List<UnauthorizedUsersDTO> usersDTO,
      String role) {

    LOG.info("Received a request to set a role for unauthorized users: " + usersDTO.toString());

    this.doctoralCenterService.setUnauthorizedUserRole(usersDTO, role);

    return send("Unauthorized user created for role: " + role);
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
  @Path("/user/delete/role/{role}")
  public Response deleteAuthUser(
      @NotBlank @Pattern(regexp = "^doctoralCenter") @CookieParam("group") String group,
      @NotBlank @Pattern(regexp = "^admin") @CookieParam("role") String roleCookie,
      String oid,
      String role) {

    LOG.info("Received a request to delete a user oid: " + oid + "for role: " + role);

    this.doctoralCenterService.deleteUser(oid, role);

    return send("User: " + oid + " removed!");
  }
}
