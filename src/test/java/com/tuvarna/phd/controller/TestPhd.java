package com.tuvarna.phd.controller;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

import com.tuvarna.phd.models.ControllerResponse;
import com.tuvarna.phd.service.PhdService;
import com.tuvarna.phd.service.dto.PhdDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import java.net.URL;
import org.junit.jupiter.api.Test;

// import org.mockito.Mockito;

@QuarkusTest
@TestHTTPEndpoint(PhdController.class)
public class TestPhd {

  @InjectMock PhdService phdService;

  @TestHTTPResource("/login")
  URL loginEndpoint;

  @Test
  void testLogin() {
    PhdDTO phdDTO = new PhdDTO("1111111", "John", "Johnatan", "Doe", "john.doe@mail.com");
    Mockito.when(this.phdService.login(phdDTO).thenReturn(true));

    given()
        .when()
        .post(loginEndpoint)
        .then()
        .statusCode(200)
        .body(is(new ControllerResponse("Phd user logged in!")));
  }
}
