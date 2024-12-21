package com.tuvarna.phd.controller;

import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.exception.DoctoralCenterException;
import com.tuvarna.phd.service.DoctoralCenterService;
import com.tuvarna.phd.service.dto.DoctoralCenterDTO;
import com.tuvarna.phd.validator.DoctoralCenterValidator;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
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
import org.jboss.logging.Logger;

@RequestScoped
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class DoctoralCenterController extends BaseController {

  private final DoctoralCenterService doctoralCenterService;
  private final DoctoralCenterValidator doctoralCenterValidator;
  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterController.class);

  @Inject
  public DoctoralCenterController(
      DoctoralCenterService doctoralCenterService,
      DoctoralCenterValidator doctoralCenterValidator) {
    this.doctoralCenterService = doctoralCenterService;
    this.doctoralCenterValidator = doctoralCenterValidator;
  }

  @POST
  @PermitAll
  @Transactional
  @Operation(
      summary = "Create Expert Doctor Center",
      description = "Creates Expert Doctor Center in the system")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Expert user created",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DoctoralCenterDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Error when trying to create expert user",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DoctoralCenterDTO.class))),
      })
  @Path("/create/expert")
  public Uni<Response> createExpert(DoctoralCenterDTO doctoralCenterDTO)
      throws DoctoralCenterException {

    LOG.info(
        "Received a request to create an expert doctor center (super admin): " + doctoralCenterDTO);
    doctoralCenterDTO.setRole("expert");
    DoctoralCenter doctoralCenter = this.doctoralCenterService.create(doctoralCenterDTO);
    LOG.info("User created!");

    LOG.info("Email is being sent to expert user: " + doctoralCenter.getEmail());
    // TODO: Remove this
    Uni<Void> emailSent =
        this.doctoralCenterService.sendEmail(doctoralCenter.getEmail(), doctoralCenter.getEmail());

    return emailSent
        .onItem()
        .transform(
            v -> {
              return send("Expert user created!");
            });
  }
}
