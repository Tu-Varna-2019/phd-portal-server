package com.tuvarna.phd.controller;

import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.exception.DoctoralCenterRoleNotFoundException;
import com.tuvarna.phd.service.DoctoralCenterService;
import com.tuvarna.phd.service.dto.DoctoralCenterDTO;
import com.tuvarna.phd.validator.DoctoralCenterValidator;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
    private final DoctoralCenterValidator doctoralCenterValidator;
    @Inject
    private final static Logger LOG = Logger.getLogger(DoctoralCenterController.class);

    @Inject
    public DoctoralCenterController(DoctoralCenterService doctoralCenterService,
            DoctoralCenterValidator doctoralCenterValidator) {
        this.doctoralCenterService = doctoralCenterService;
        this.doctoralCenterValidator = doctoralCenterValidator;
    }

    @POST
    @Transactional
    @PermitAll
    @Operation(summary = "Create Expert Doctor Center", description = "Creates Expert Doctor Center in the system")
    @APIResponses(value = @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctoralCenterDTO.class))))
    @Path("/create/expert")
    public Uni<Void> createExpert(DoctoralCenterDTO doctoralCenterDTO) throws DoctoralCenterRoleNotFoundException {
        LOG.info("Received a request to create an expert doctor center (super admin): " + doctoralCenterDTO);
        DoctoralCenter doctoralCenter = this.doctoralCenterService.create(doctoralCenterDTO);

        // NOTE: Send email only for expert role
        if (doctoralCenter.getRole().getRole().equals("expert")) {
            LOG.info("Email sent to expert user: " + doctoralCenter.getEmail());
            return doctoralCenterService.sendEmail(doctoralCenter.getEmail(), doctoralCenter.getPassword());
        }
        ;
        return null;
    }

    @PUT
    @Transactional
    @PermitAll
    @Operation(summary = "Reset password for doctoral center member", description = "Reset password for doctoral center member")
    @APIResponses(value = @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctoralCenterDTO.class))))
    @Path("/change/password")
    public void changePassword(DoctoralCenterDTO doctoralCenterDTO) throws DoctoralCenterRoleNotFoundException {
        LOG.info("Received a request to change password for  doctor center member: " + doctoralCenterDTO);
    };

    @POST
    @Transactional
    @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Create a Manager Doctor Center", description = "Creates Manager Doctor Center in the system")
    @APIResponses(value = @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctoralCenterDTO.class))))
    @Path("/create/manager")
    public void createManager(DoctoralCenterDTO doctoralCenterDTO) throws DoctoralCenterRoleNotFoundException {
        LOG.info("Received a request to create a manager doctor center expert (admin): " + doctoralCenterDTO);
        DoctoralCenter doctoralCenter = this.doctoralCenterService.create(doctoralCenterDTO);

        // TODO: add something
    }
}
