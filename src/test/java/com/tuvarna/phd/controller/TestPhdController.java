package com.tuvarna.phd.controller;

import static org.mockito.Mockito.when;

import com.tuvarna.phd.models.ControllerResponse;
import com.tuvarna.phd.service.dto.PhdDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(PhdController.class)
public class TestPhdController {

  @InjectMock PhdController phdController;

  static PhdDTO phdDTO;

  @BeforeAll
  static void init() {
    phdDTO = new PhdDTO("1111111", "John", "Johnatan", "Doe", "john.doe@mail.com");
  }

  @Test
  void testLogin() {
    when(phdController.login(phdDTO))
        .thenReturn(Response.ok().entity(new ControllerResponse("Phd user logged in!")).build());

    // given()
    //     .contentType(ContentType.JSON)
    //     .body(phdDTO)
    //     .when()
    //     .post("/login")
    //     .then()
    //     .statusCode(200)
    //     .body("message", is("Phd user logged in!"));
    //
    // verify(phdController).login(phdDTO);
  }
}
