package com.tuvarna.phd.controller.storage;

import com.tuvarna.phd.controller.BaseController;
import com.tuvarna.phd.service.dto.storage.BlobDataDTO;
import com.tuvarna.phd.service.storage.S3ClientService;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.jboss.logging.Logger;

@RequestScoped
@Path("/file")
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class FileUploadController extends BaseController {

  private final S3ClientService s3ClientService;
  @Inject private Logger log;

  @Inject
  public FileUploadController(S3ClientService s3ClientService) {
    this.s3ClientService = s3ClientService;
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Operation(summary = "Upload a file", description = "Upload a file to s3")
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
            description = "File not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlobDataDTO.class)))
      })
  @Path("/upload")
  public Response upload(BlobDataDTO blob) {
    log.info("Received a request to upload a file: " + blob.filename);
    this.s3ClientService.upload(blob);

    return send("File uploaded!", 201);
  }

  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Operation(summary = "Download blob file", description = "Download an s3 blob file")
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
            description = "File not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlobDataDTO.class)))
      })
  @Path("/download")
  public Response download() {
    log.info("Received a request to download files");
    // TODO: Change this to obj key
    String objectKey = "lol";

    Tuple2<byte[], String> files = this.s3ClientService.download(objectKey);
    byte[] fileBytes = files.getItem1();
    String contentType = files.getItem2();

    ResponseBuilder response = builder(fileBytes, "Files downloaded!");
    response.header("Content-Disposition", "attachment;filename=" + objectKey);
    response.header("Content-Type", contentType);

    return response.build();
  }
}
