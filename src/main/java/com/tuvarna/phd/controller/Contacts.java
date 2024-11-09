package com.tuvarna.phd.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/contacts")
public class Contacts {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getContacts() {
    return "Teachers";
  }
}
