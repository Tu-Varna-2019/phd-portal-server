package com.tuvarna.phd.controller;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
// import static org.mockito.Mockito.verify;

import com.tuvarna.phd.service.PhdService;
import com.tuvarna.phd.service.dto.PhdDTO;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(PhdController.class)
public class TestPhd {

  @Inject PhdService phdService;

  @Test
  void testLogin() {
    PhdDTO phdDTO = new PhdDTO("1111111", "John", "Johnatan", "Doe", "john.doe@mail.com");
    // Mockito.doNothing().when(phdServiceMock).login(phdDTO);

    given()
        .contentType(ContentType.JSON)
        .body(phdDTO)
        .when()
        .post("/login")
        .then()
        .statusCode(200)
        .body("message", is("Phd user logged in!"));

    // verify(phdService).login(phdDTO);
  }
}
