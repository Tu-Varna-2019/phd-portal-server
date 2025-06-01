package com.tuvarna.phd.controller;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.UnauthorizedDTO;
import com.tuvarna.phd.entity.Commission;
import com.tuvarna.phd.entity.Unauthorized;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.service.DoctoralCenterService;
import com.tuvarna.phd.validator.CandidateValidator;
import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
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
@Path("/doctoralcenter")
@Tag(
    name = "Doctoral center endpoint",
    description = "Endpoint for serving doctoral center services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public final class DoctoralCenterController extends BaseController {

  private final DoctoralCenterService doctoralCenterService;
  private final CandidateValidator candidateValidator;
  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterController.class);

  @Inject
  public DoctoralCenterController(
      DoctoralCenterService doctoralCenterService, CandidateValidator candidateValidator) {
    this.doctoralCenterService = doctoralCenterService;
    this.candidateValidator = candidateValidator;
  }

  @PATCH
  @Operation(
      summary = "Candidate application",
      description = "Approve or reject candidate for exams")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Candidate approved",
            content = @Content(mediaType = "application/json")),
        @APIResponse(
            responseCode = "400",
            description = "Error when approving/rejecting candidate",
            content = @Content(mediaType = "application/json")),
      })
  @Path("/candidate/{email}/application/{status}")
  public Response review(@PathParam("email") String email, @PathParam("status") String status) {
    this.candidateValidator.validateStatusExists(status);

    LOG.info("Received a request to " + status + " candidate: " + email);

    try {
      this.doctoralCenterService.review(email, status);
    } catch (IOException exception) {
      LOG.error("Error in reading mail template: " + exception);
      throw new HttpException("Error in sending email. Please try again later!");
    }

    return send("Candidate's applicaton is changed to: " + status);
  }

  @GET
  @Operation(summary = "Get all candidates", description = "Get all candidates")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Candidates retrieved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CandidateDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when approving/rejecting phd's candidate",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CandidateDTO.class))),
      })
  @Path("/candidates")
  public Response getCandidates(@NotNull @RestQuery String fields) {
    if (fields == null || fields.isEmpty()) {
      LOG.warn("Client requested to retrieve all candidates with empty fields");
      throw new HttpException("Fields for candidate cannot be empty!");
    }

    LOG.info("Received a request to retrieve all candidates with fields: " + fields);
    List<CandidateDTO> candidates = this.doctoralCenterService.getCandidates(fields);

    return send("Candidates retrieved!", candidates);
  }

  @GET
  @Operation(
      summary = "Unauthorized users",
      description = "Get all users, that attempted to sign in to the Phd platform and are allowed")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "All unauthorized users retrieved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Unauthorized.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving unauthorized users!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Unauthorized.class))),
      })
  @Path("/unauthorized-users")
  public Response getUnauthorizedUsers() {
    LOG.info("Received a request to get all unauthorized users that have allowed status");

    List<Unauthorized> unauthorizedUsers = this.doctoralCenterService.getUnauthorizedUsers();

    unauthorizedUsers.removeIf((user) -> user.getAllowed() == false);

    LOG.info("Unauthorized users with allowed status received!");
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
                    schema = @Schema(implementation = UnauthorizedDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when setting a group for a unauthorized users!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnauthorizedDTO.class))),
      })
  @Path("/unauthorized-users/group")
  public Response setRoleForUnauthorizedUsers(
      List<UnauthorizedDTO> usersDTO, @RestQuery String group) {

    LOG.info("Received a request to set a group for unauthorized users: " + usersDTO.toString());
    this.doctoralCenterService.setUnauthorizedUserGroup(usersDTO, group);

    return send("Unauthorized user is set for group: " + group);
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
    List<String> docCenterRoles = this.doctoralCenterService.getDoctoralCenterRoles();

    return send("All doctoral center roles retrieved!", docCenterRoles);
  }

  @GET
  @Operation(summary = "Get all grades", description = "Get all grades")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "grades retrieved",
            content = @Content(mediaType = "application/json")),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving grades!",
            content = @Content(mediaType = "application/json")),
      })
  @Path("/grades")
  public Response getExams() {
    LOG.info("Received a controller request to retrieve all grades.");
    List<GradeDTO> grades = this.doctoralCenterService.getExams();

    return send("Grades retrieved", grades);
  }

  @GET
  @Operation(summary = "Get commision", description = "Get commision")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Commision retrieved!",
            content = @Content(mediaType = "application/json")),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving commision!",
            content = @Content(mediaType = "application/json")),
      })
  @Path("/commision")
  public Response getCommision() {
    LOG.info("Received a controller request to retrieve all commisions.");
    List<Commission> exams = this.doctoralCenterService.getCommision();

    return send("Commision retrieved" + exams);
  }
}
