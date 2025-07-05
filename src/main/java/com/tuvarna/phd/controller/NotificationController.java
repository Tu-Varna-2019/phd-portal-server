package com.tuvarna.phd.controller;

import com.tuvarna.phd.dto.IdDTO;
import com.tuvarna.phd.dto.NotificationClientDTO;
import com.tuvarna.phd.dto.NotificationDTO;
import com.tuvarna.phd.service.NotificationService;
import com.tuvarna.phd.validator.NotificationValidator;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

@RequestScoped
@Path("/notify")
@Tag(name = "Notification endpoint", description = "Endpoint for serving notifications  services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public final class NotificationController extends BaseController {

  @Inject private Logger LOG = Logger.getLogger(LogController.class);
  @Inject JsonWebToken jwt;
  @Inject NotificationService notificationService;

  @Inject NotificationValidator notificationValidator;

  @POST
  @Operation(summary = "Save notification", description = "Save a notification from user action")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "notification saved!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotificationDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when saving notification",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotificationDTO.class))),
      })
  public Response save(NotificationDTO notificationDTO) {
    this.notificationValidator.validateSeverityExists(notificationDTO);
    this.notificationValidator.validateScopesExists(notificationDTO);
    LOG.info("Notification scope and severity is valid, moving on...");

    this.notificationService.save(notificationDTO);
    LOG.info("Notification saved!");

    return send("Notification saved!");
  }

  @GET
  @Operation(summary = "Get notifications", description = "Get notifications, depending on a role")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "notification retrived!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotificationDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when retreiving notification",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotificationDTO.class))),
      })
  public Response get() {
    String oid = jwt.getClaim("oid");
    List<NotificationClientDTO> notifications = this.notificationService.get(oid);

    return send("Notifications retrieved", notifications);
  }

  @DELETE
  @Operation(summary = "delete notifications", description = "Delete notifications")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Нотификации са изтрити успешно!",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotificationDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when deleting notifications",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotificationDTO.class))),
      })
  public Response delete(List<IdDTO> notificationIds) {
    LOG.info("Received a request to delete notifications");
    this.notificationService.delete(notificationIds);

    return send("Нотификации са изтрити успешно!");
  }
}
