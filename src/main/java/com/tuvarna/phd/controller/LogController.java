package com.tuvarna.phd.controller;

import com.tuvarna.phd.mapper.LogMapper;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.service.LogService;
import com.tuvarna.phd.service.dto.LogDTO;
import com.tuvarna.phd.service.dto.UserPrincipalDTO;
import com.tuvarna.phd.service.dto.sendLogDTO;
import com.tuvarna.phd.validator.LogValidator;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.jboss.logging.Logger;

@RequestScoped
// TODO: restrict the endpoint usage only to the admin ??
@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public class LogController extends BaseController {

  private LogService logService;
  private LogValidator logValidator;
  private DoctoralCenterRepository doctoralCenterRepository;
  private LogMapper logMapper;
  @Inject private Logger LOG = Logger.getLogger(LogController.class);
  @Inject JsonWebToken jwt;

  @Inject
  public LogController(
      LogService logService,
      LogValidator logValidator,
      LogMapper logMapper,
      DoctoralCenterRepository doctoralCenterRepository) {
    this.logService = logService;
    this.logValidator = logValidator;
    this.doctoralCenterRepository = doctoralCenterRepository;
    this.logMapper = logMapper;
  }

  @POST
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
  public Response save(
      sendLogDTO sendLogDTO, @CookieParam("group") String group, @CookieParam("role") String role) {
    String userGroup = group.equals("doctoralCenter") ? role : group;

    this.logValidator.validateGroupExists(userGroup);
    this.logValidator.validateLevel(sendLogDTO.getLevel());

    String oid = jwt.getClaim("oid");
    String name = jwt.getClaim("name");
    String email = jwt.getName();

    LogDTO logDTO = this.logMapper.toDto(sendLogDTO);
    logDTO.setUserPrincipalDTO(new UserPrincipalDTO(oid, name, email, userGroup));

    this.logService.save(logDTO);
    LOG.info("Log saved!");

    return send("Log saved!");
  }

  @GET
  @Operation(summary = "Get logs", description = "Get logs, depending on a role")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Log retrived!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LogDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retriving log",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LogDTO.class))),
      })
  public Response get() {
    String email = jwt.getName();
    String group = (this.doctoralCenterRepository.getByEmail(email)).getRole().getRole();
    LOG.info("Received a request to retrieve log from user group: " + group);

    List<LogDTO> logs = this.logService.get(group);

    return send("Logs retrieved", logs);
  }

  @DELETE
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
  public Response delete(List<LogDTO> logs) {
    LOG.info("Received a request to delete logs");
    this.logService.delete(logs);

    return send("Logs deleted");
  }
}
