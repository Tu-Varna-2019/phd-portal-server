package com.tuvarna.phd.controller;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.PhdDTO;
import com.tuvarna.phd.service.DoctoralCenterService;
import com.tuvarna.phd.validator.DoctoralCenterValidator;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterController.class);

  @Inject
  public DoctoralCenterController(
      DoctoralCenterService doctoralCenterService,
      DoctoralCenterValidator doctoralCenterValidator) {
    this.doctoralCenterService = doctoralCenterService;
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
                    schema = @Schema(implementation = PhdDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when approving/rejecting phd's candidate",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PhdDTO.class))),
      })
  @Path("/candidate/{oid}/status")
  public Uni<Response> updateCandidateStatus(CandidateDTO candidateDTO, String oid) {
    LOG.info(
        "Received a request to change candidate's"
            + oid
            + "status to: "
            + candidateDTO.getStatus());

    this.doctoralCenterService.updateCandidateStatus(candidateDTO, oid);

    if (candidateDTO.getStatus().equals("approved")) {

      LOG.info("Email is being sent for the candidate: " + candidateDTO.getEmail());
      Uni<Void> emailSent = this.doctoralCenterService.sendEmail(candidateDTO.getEmail());

      return emailSent
          .onItem()
          .transform(
              v -> {
                return send("Candidate email sent!");
              });
    }
    return Uni.createFrom().item(send("Canidatestatus changed to: " + candidateDTO.getStatus()));
  }
}
