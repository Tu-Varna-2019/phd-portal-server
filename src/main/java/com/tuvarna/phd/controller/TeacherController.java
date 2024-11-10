package com.tuvarna.phd.controller;

import com.tuvarna.phd.entity.Teacher;
import com.tuvarna.phd.service.TeacherService;
import com.tuvarna.phd.service.dto.TeacherDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

// import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
// import org.eclipse.microprofile.openapi.annotations.Operation;
// import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@RequestScoped
@Path("/teacher")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
// @SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme =
// "basic")
public class TeacherController {

  private final TeacherService teacherService;

  @Inject
  public TeacherController(TeacherService teacherService) {
    this.teacherService = teacherService;
  }

  @POST
  @PermitAll
  @Transactional
  @Path("/create")
  public Teacher createTeacher(TeacherDTO teacherDTO) {
    return this.teacherService.save(teacherDTO.toTeacher());
  }
}
