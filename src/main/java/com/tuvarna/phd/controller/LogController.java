package com.tuvarna.phd.controller;

import com.tuvarna.phd.service.LogService;
import com.tuvarna.phd.service.dto.LogDTO;
import com.tuvarna.phd.validator.DoctoralCenterValidator;
import com.tuvarna.phd.validator.LogValidator;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class LogController extends BaseController {

  private  LogService logService;
  private LogValidator logValidator;
  private  DoctoralCenterValidator dValidator;
  @Inject private  Logger LOG = Logger.getLogger(LogController.class);

  @Inject
  public LogController(
      LogService logService, LogValidator logValidator, DoctoralCenterValidator dValidator) {
    this.logService = logService;
    this.logValidator = logValidator;
    this.dValidator = dValidator;
  }

  @POST
  @Transactional
  @Operation(summary = "Save log", description = "Save a log from user action")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Log saved!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LogDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when saving log",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LogDTO.class))),
      })
  @Path("/save")
  public Response save(LogDTO logDTO) {
    LOG.info(
        "Received a request to save log from user role: " + logDTO.getUserPrincipalDTO().getRole());
    this.logValidator.validateRoleExists(logDTO);

    this.logService.save(logDTO);

    LOG.info("Log saved!");

    return send("Log saved!");
  }

  @GET
  @Transactional
  @Operation(summary = "fetch logs", description = "Fetch logs, depending on a role")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Log fetched!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LogDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when fetching log",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LogDTO.class))),
      })
  @Path("/fetch/{role}")
  public Response fetch(String role) {
    LOG.info("Received a request to fetch log from user role: " + role);
    this.dValidator.validateRole(role);
    List<LogDTO> logs = this.logService.fetch(role);

    return send("Logs fetched", logs);
  }

  @DELETE
  @Transactional
  @Operation(summary = "delete logs", description = "Delete logs")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Logs deleted!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LogDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when deleting logs",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LogDTO.class))),
      })
  @Path("/delete")
  public Response delete(List<LogDTO> logs) {
    LOG.info("Received a request to delete logs");
    this.logService.deleteLogs(logs);

    return send("Logs deleted");
  }
}
