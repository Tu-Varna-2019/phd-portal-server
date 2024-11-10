package com.tuvarna.phd.controller;

import com.tuvarna.phd.entity.Teacher;
import com.tuvarna.phd.exception.TeacherNotFoundException;
import com.tuvarna.phd.service.TeacherService;
import com.tuvarna.phd.service.dto.TeacherDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

@RequestScoped
@Path("/teacher")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class TeacherController {

  private final TeacherService teacherService;

  @Inject
  public TeacherController(TeacherService teacherService) {
    this.teacherService = teacherService;
  }

  @POST
  @PermitAll
  @Transactional
  @RolesAllowed({"USER", "ADMIN"})
  @Operation(summary = "Create teacher", description = "Creates a teacher in the system")
  @APIResponses(
      value =
          @APIResponse(
              responseCode = "200",
              description = "Success",
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = TeacherDTO.class))))
  @Path("/create")
  public Teacher createTeacher(TeacherDTO teacherDTO) {
    return this.teacherService.save(teacherDTO.toTeacher());
  }

  @GET
  @PermitAll
  @Transactional
  @RolesAllowed({"USER", "ADMIN"})
  @Operation(summary = "Get a teacher", description = "Get a teacher from the system")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Success",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TeacherDTO.class))),
        @APIResponse(
            responseCode = "404",
            description = "Teacher not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TeacherNotFoundException.class)))
      })
  @Path("/get")
  // TODO: Finish this
  public Teacher getTeacher(TeacherDTO teacherDTO) throws TeacherNotFoundException {
    return this.teacherService.save(teacherDTO.toTeacher());
  }
}
