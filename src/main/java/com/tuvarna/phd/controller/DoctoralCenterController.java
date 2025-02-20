package com.tuvarna.phd.controller;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CandidateStatusDTO;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.exception.CandidateException;
import com.tuvarna.phd.service.DoctoralCenterService;
import com.tuvarna.phd.validator.CandidateValidator;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
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
  @Operation(summary = "Phd's candidate", description = "Approve or reject phd's candidate")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Phd approved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CandidateStatusDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when approving/rejecting phd's candidate",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CandidateStatusDTO.class))),
      })
  @Path("/candidate/status")
  public Response updateCandidateStatus(CandidateStatusDTO candidateDTO) {
    this.candidateValidator.validateStatusExists(candidateDTO.getStatus());
    LOG.info(
        "Received a request to change candidate's email: "
            + candidateDTO.getEmail()
            + " status to: "
            + candidateDTO.getStatus());

    try {
      this.doctoralCenterService.review(candidateDTO);
    } catch (IOException exception) {
      LOG.error("Error in reading mail template: " + exception);
      throw new CandidateException("Error in sending email. Please try again later!");
    }

    return send("Candidate status changed to: " + candidateDTO.getStatus());
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
  public Response getCandidates() {
    LOG.info("Received a request to retrieve all candidates");
    List<CandidateDTO> candidates = this.doctoralCenterService.getCandidates();

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
                    schema = @Schema(implementation = UnauthorizedUsers.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving unauthorized users!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnauthorizedUsers.class))),
      })
  @Path("/unauthorized-users")
  public Response getUnauthorizedUsers() {
    LOG.info("Received a request to get all unauthorized users that have allowed status");

    List<UnauthorizedUsers> unauthorizedUsers = this.doctoralCenterService.getUnauthorizedUsers();

    unauthorizedUsers.removeIf((user) -> user.getIsAllowed() == false);

    LOG.info("Unauthorized users with allowed status received!");
    return send("Unauthorized users retrieved!", unauthorizedUsers);
  }
}
