package com.tuvarna.phd.controller;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.dto.SubjectDTO;
import com.tuvarna.phd.entity.Faculty;
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
import org.jboss.resteasy.reactive.RestQuery;

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
  @Path("/apply")
  public Response apply(CandidateDTO candidateDTO) {
    LOG.info("Received a candidate request to apply as email: " + candidateDTO.getEmail());

    this.candidateService.apply(candidateDTO);
    return send("Candidate application finished successfully!");
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
            description = "Error in retrieving curriculums",
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

  @GET
  @Operation(summary = "Get subjects", description = "Get subjects")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Subjects retrieved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubjectDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving subjects",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubjectDTO.class))),
      })
  @Path("/subjects")
  public Response getSubjects(@RestQuery String curriculumName) {
    LOG.info("Received a request to retrieve all subjects from curriculumName: " + curriculumName);
    List<SubjectDTO> subjectDTOs = this.candidateService.getSubjects(curriculumName);

    return send("Subjects retrieved", subjectDTOs);
  }

  @GET
  @Operation(summary = "Get phd contests", description = "Get phd contests")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Phd contests retrieved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CandidateDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving phd contests",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CandidateDTO.class))),
      })
  @Path("/contests")
  public Response getContests() {
    LOG.info("Received a request to retrieve all constests for accepted candidates into phd");
    List<CandidateDTO> candidateDTOs = this.candidateService.getContests();

    return send("Accepted phd candidates retrieved!", candidateDTOs);
  }

  @GET
  @Operation(summary = "Get candidates in review", description = "Get candidates in review")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Candidates in review retrieved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CandidateDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving candidates in review!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CandidateDTO.class))),
      })
  @Path("/in-review")
  public Response getCandidatesInReview() {
    LOG.info("Received a request to retrieve all candidates that are currently in review");
    List<CandidateDTO> candidateDTOs = this.candidateService.getCandidatesInReview();

    return send("Accepted phd candidates retrieved!", candidateDTOs);
  }

  @GET
  @Operation(summary = "Get faculties", description = "Get faculties")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Faculties retrieved",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retrieving faculties",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
      })
  @Path("/faculty")
  public Response getFaculties() {
    LOG.info("Received a request to retrieve all faculties");
    List<Faculty> faculties = this.candidateService.getFaculties();

    return send("Faculties retrieved", faculties);
  }
}
