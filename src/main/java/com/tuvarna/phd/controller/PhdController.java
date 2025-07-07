package com.tuvarna.phd.controller;

import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.SubjectDTO;
import com.tuvarna.phd.entity.Faculty;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.service.PhdService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.jboss.resteasy.reactive.RestQuery;

@RequestScoped
@Path("/phd")
@Tag(name = "Phd endpoint", description = "Endpoint for serving phd services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public final class PhdController extends BaseController {

  @Inject PhdService phdService;
  @Inject JsonWebToken jwt;
  @Inject Logger LOG = Logger.getLogger(PhdController.class);

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
    List<CurriculumDTO> curriculumDTOs = this.phdService.getCurriculums();

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
  public Response getSubjects(
      @RestQuery Optional<String> curriculumName, @RestQuery Optional<String> facultyName) {
    List<SubjectDTO> subjectDTOs = new ArrayList<SubjectDTO>();

    if (curriculumName.isPresent() && facultyName.isPresent()) {
      throw new HttpException("Cannot have both curriculum and faculty name at the same time!");
    } else if (curriculumName.isPresent()) {
      LOG.info(
          "Received a request to retrieve all subjects by curriculum name: "
              + curriculumName.get());
      subjectDTOs = this.phdService.getSubjectsByCurriculum(curriculumName.get());
    } else if (facultyName.isPresent()) {
      LOG.info("Received a request to retrieve all subjects by faculty name: " + facultyName.get());
      subjectDTOs = this.phdService.getSubjectsByFaculty(facultyName.get());
    } else {
      throw new HttpException(
          "Cannot retrieve subject without provided curriculum or faculty name");
    }
    ;

    LOG.info("Subjects retrieved: " + subjectDTOs.toString());
    return send("Subjects retrieved", subjectDTOs);
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
    List<Faculty> faculties = this.phdService.getFaculties();

    return send("Faculties retrieved", faculties);
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
  public Response getGrades() {
    LOG.info("Received a controller request to retrieve all grades.");

    String oid = jwt.getClaim("oid");
    List<GradeDTO> grades = this.phdService.getGrades(oid);

    return send("Grades retrieved", grades);
  }

  @PATCH
  @Operation(summary = "Set attachment to grade", description = "Set attachment to grade")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Attachments set to grade successfully!",
            content = @Content(mediaType = "application/json")),
        @APIResponse(
            responseCode = "400",
            description = "Error when setting attachments to grade!",
            content = @Content(mediaType = "application/json")),
      })
  @Path("/grade/{id}")
  public Response setAttachmentsToGrade(List<String> attachments, @PathParam("id") Long gradeId) {
    LOG.info("Received a controller request to set attachments to grade " + gradeId);

    this.phdService.setAttachmentToGrade(gradeId, attachments);

    return send("Attachments set for grade id: " + gradeId);
  }
}
