package com.tuvarna.phd.controller;

import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.exception.DoctoralCenterRoleNotFoundException;
import com.tuvarna.phd.service.DoctoralCenterService;
import com.tuvarna.phd.service.dto.DoctoralCenterDTO;

import io.smallrye.common.annotation.Blocking;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
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
import org.jboss.logging.Logger;

@RequestScoped
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class DoctoralCenterController {

    private final DoctoralCenterService doctoralCenterService;
    @Inject
    private final static Logger LOG = Logger.getLogger(DoctoralCenterController.class);

    @Inject
    public DoctoralCenterController(DoctoralCenterService doctoralCenterService) {
        this.doctoralCenterService = doctoralCenterService;
    }

    @POST
    @Transactional
    @PermitAll
    // @RolesAllowed({"USER", "ADMIN"})
    // @Authenticated
    @Operation(summary = "Create Expert/Manager Doctor Center", description = "Creates  Expert or Manager Doctor Center in the system")
    @APIResponses(value = @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctoralCenterDTO.class))))
    @Path("/create")
    public void createExpert(DoctoralCenterDTO doctoralCenterDTO) throws DoctoralCenterRoleNotFoundException {
        LOG.info("Received a request to create doctor center expert (super admin): " + doctoralCenterDTO);

        DoctoralCenter doctoralCenter = this.doctoralCenterService.create(doctoralCenterDTO);

        // NOTE: Send email only for expert role
        if (doctoralCenter.getRole().getRole().equals("expert")) {
            doctoralCenterService.sendEmail(doctoralCenter.getEmail(), doctoralCenter.getPassword());
            LOG.info("Email sent to expert user: " + doctoralCenter.getEmail());
        }
        ;

    }
}
