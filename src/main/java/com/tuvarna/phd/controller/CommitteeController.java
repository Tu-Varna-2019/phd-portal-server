package com.tuvarna.phd.controller;

import com.sun.istack.NotNull;
import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.service.CommitteeService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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

@RequestScoped
@Path("/committee")
@Tag(name = "Committee endpoint", description = "Endpoint for serving Committee services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    securitySchemeName = "Bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer")
public final class CommitteeController extends BaseController {

  private final CommitteeService committeeService;
  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterController.class);

  @Inject
  public CommitteeController(CommitteeService committeeService) {
    this.committeeService = committeeService;
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
      LOG.warn("Client requested to retrieve all candidates with fields");
      throw new HttpException("Fields for candidate cannot be empty!");
    }

    LOG.info("Received a request to retrieve all candidates with fields: " + fields);
    List<CandidateDTO> candidates = this.committeeService.getCandidates(fields);

    return send("Candidates retrieved!", candidates);
  }
}
