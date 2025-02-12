package com.tuvarna.phd.controller;

import com.tuvarna.phd.service.S3ClientService;
import com.tuvarna.phd.dto.BlobDataDTO;
import com.tuvarna.phd.dto.FileBlobDTO;
import com.tuvarna.phd.dto.FilenameDTO;
import com.tuvarna.phd.validator.S3ClientValidator;
import io.smallrye.common.constraint.NotNull;
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
import jakarta.ws.rs.core.Response.ResponseBuilder;
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
import org.jboss.resteasy.reactive.RestCookie;
import org.jboss.resteasy.reactive.RestQuery;

@RequestScoped
@Path("/file")
@Tag(name="File endpoint", description = "Endpoint for serving file services")
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
public final class FileController extends BaseController {

  private final S3ClientService s3ClientService;
  private final S3ClientValidator s3ClientValidator;
  @Inject private Logger LOG;
  @Inject JsonWebToken jwt;

  @Inject
  public FileController(S3ClientService s3ClientService, S3ClientValidator s3ClientValidator) {
    this.s3ClientService = s3ClientService;
    this.s3ClientValidator = s3ClientValidator;
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Upload a file", description = "Upload a file to s3")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Success",
            content =
                @Content(
                    mediaType = "multipart/form-data",
                    schema = @Schema(implementation = BlobDataDTO.class))),
        @APIResponse(
            responseCode = "404",
            description = "file not found",
            content =
                @Content(
                    mediaType = "multipart/form-data",
                    schema = @Schema(implementation = BlobDataDTO.class)))
      })
  @Path("/upload")
  public Response upload(BlobDataDTO file, @RestQuery String type, @RestCookie String group) {
    this.s3ClientValidator.validateType(type);
    String oid = jwt.getClaim("oid");

    LOG.info(
        "Received a controller request to upload a file "
            + group
            + " with filename: "
            + file.getFilename());

    file.generateUniqueFilename();
    this.s3ClientService.upload(file, oid, type);
    if (type.equals("avatar")) {
      String oldPicture = this.s3ClientService.getPictureByOid(group, oid);
      if (!oldPicture.isEmpty()) this.s3ClientService.delete(oid, group, type, oldPicture);
      this.s3ClientService.setPictureByOid(file.getUniqueFilename(), group, oid);
    }

    return send("file uploaded!", file.getUniqueFilename(), 201);
  }

  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Operation(summary = "Download file", description = "Download an s3 file")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Success",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlobDataDTO.class))),
        @APIResponse(
            responseCode = "404",
            description = "file not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlobDataDTO.class)))
      })
  @Path("/download")
  public Response download(@RestQuery String type, @RestQuery String filename) {
    LOG.info("Received a request to download file from type: " + type);
    String oid = jwt.getClaim("oid");
    String email = jwt.getClaim("name");

    String objectKey = oid + "/" + type + "/" + filename;
    FileBlobDTO fileBlobDTO = this.s3ClientService.download(objectKey);

    ResponseBuilder response = builder(fileBlobDTO.getData());
    response.header("Content-Disposition", "attachment;filename=" + email);
    response.header("Content-Type", fileBlobDTO.getMimetype());

    LOG.info(
        "Downloaded s3 file successfully for filename: "
            + filename
            + " . Now sending it back to client...");

    return response.build();
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Delete a file", description = "Deletes a file from s3 bucket")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "Success",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FilenameDTO.class))),
        @APIResponse(
            responseCode = "404",
            description = "file not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FilenameDTO.class)))
      })
  public Response delete(
      FilenameDTO file, @RestCookie String group, @NotNull @RestQuery String type) {
    String oid = jwt.getClaim("oid");

    LOG.info(
        "Received a controller request to delete a file for user"
            + oid
            + " ,which corresponds to a group: "
            + group);

    this.s3ClientService.delete(oid, group, type, file.getFilename());

    if (type.equals("avatar")) {
      this.s3ClientService.setPictureByOid("", group, oid);
    }

    return send("File deleted!");
  }
}
