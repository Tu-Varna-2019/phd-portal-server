package com.tuvarna.phd.controller;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.service.CandidateService;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

@PermitAll
@RequestScoped
@Path("/candidate")
@Tag(name = "Candidate endpoint", description = "Endpoint for serving candidate services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public final class CandidateController extends BaseController {

  private final CandidateService candidateService;
  @Inject private Logger LOG = Logger.getLogger(CandidateController.class);

  @Inject
  public CandidateController(CandidateService candidateService) {
    this.candidateService = candidateService;
  }

  @POST
  @Operation(
      summary = "Register to phd as a candidate",
      description = "Register to phd as a candidate")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Candidate registered",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CandidateDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when registering to phd as a candidate",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CandidateDTO.class))),
      })
  @Path("/register")
  public Response register(CandidateDTO candidateDTO) {
    LOG.info("Received a candidate request to register as email: " + candidateDTO.getEmail());

    this.candidateService.register(candidateDTO);
    return send("Registration finished successfully!");
  }

  @GET
  @Operation(summary = "Get curriculums", description = "Get curriculums")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Curriculums retrieved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CurriculumDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when approving/rejecting phd's candidate",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CurriculumDTO.class))),
      })
  @Path("/curriculums")
  public Response getCurriculums() {
    LOG.info("Received a request to retrieve all curriculums");
    List<CurriculumDTO> curriculumDTOs = this.candidateService.getCurriculums();

    return send("Curriculums retrieved", curriculumDTOs);
  }
}
