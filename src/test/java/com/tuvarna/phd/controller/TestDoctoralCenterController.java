package com.tuvarna.phd.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.service.DoctoralCenterService;
import com.tuvarna.phd.validator.CandidateValidator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(DoctoralCenterController.class)
public class TestDoctoralCenterController {

  @Inject DoctoralCenterController doctoralCenterController;
  @InjectMock DoctoralCenterService doctoralCenterService;
  CandidateValidator candidateValidator;

  @Test
  void testReview() throws IOException {
    String email = "john.doe@mail.com";
    String status = "rejected";
    doNothing().when(doctoralCenterService).review(anyString(), anyString());

    given()
        .contentType(ContentType.JSON)
        .pathParam("email", email)
        .pathParam("status", status)
        .when()
        .patch("/candidate/{email}/application/{status}")
        .then()
        .statusCode(200)
        .body("message", is("Candidate's applicaton is changed to: " + status));

    verify(doctoralCenterService).review(email, status);
  }

  @Test
  void testSetCommissionOnGrade() {
    Long id = 1L;
    String name = "commission1";
    doNothing().when(doctoralCenterService).setCommissionOnGrade(id, name);

    given()
        .contentType(ContentType.JSON)
        .pathParam("id", id)
        .pathParam("name", name)
        .when()
        .patch("/grade/{id}/commission/{name}")
        .then()
        .statusCode(200)
        .body("message", is("Grade updated with commision: " + name + " !"));

    verify(doctoralCenterService).setCommissionOnGrade(id, name);
  }

  @Test
  void testGetCandidates() {
    String fields = "name";
    List<CandidateDTO> candidateDTOs =
        List.of(
            new CandidateDTO(
                "John",
                "john.doe@mail.com",
                "1",
                "111111111",
                "Bulgaria",
                "Varna",
                "Primorski, 9002 Varna, Bulgaria",
                "9000",
                2025L,
                "biography113.jpeg",
                "curriculum1",
                "faculty1",
                "enrolled"));

    when(doctoralCenterService.getCandidates(fields)).thenReturn(candidateDTOs);
    Response response = doctoralCenterController.getCandidates(fields);
    assertEquals(200, response.getStatus());

    ControllerResponse controllerResponse = (ControllerResponse) response.getEntity();
    assertEquals("Candidates retrieved!", controllerResponse.message());
    assertEquals(candidateDTOs, controllerResponse.data());
  }
}
