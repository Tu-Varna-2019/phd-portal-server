package com.tuvarna.phd.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tuvarna.phd.dto.CandidateApplyDTO;
import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CurriculumCreateDTO;
import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.service.CandidateService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(CandidateController.class)
public class TestCandidateController {

  @Inject CandidateController candidateController;
  @InjectMock CandidateService candidateService;
  static CandidateApplyDTO candidateApplyDTO;

  @BeforeAll
  static void init() {
    candidateApplyDTO =
        new CandidateApplyDTO(
            "John",
            "john.doe@mail.com",
            "1111111",
            "Bulgaria",
            "Varna",
            "Primorski, 9002 Varna, Bulgaria",
            "biography113.jpg",
            2025L,
            9000L,
            "faculty1",
            "enrolled",
            new CurriculumCreateDTO(
                "Curriculum123",
                "regular",
                "faculty1",
                Arrays.asList("discipline1", "discipline2")));
  }

  @Test
  void testApply() {
    doNothing().when(candidateService).apply(any());

    given()
        .contentType(ContentType.JSON)
        .body(candidateApplyDTO)
        .when()
        .post("/apply")
        .then()
        .statusCode(200)
        .body("message", is("Candidate application finished successfully!"));

    verify(candidateService).apply(candidateApplyDTO);
  }

  @Test
  void testInReview() {
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

    when(candidateService.getCandidatesInReview()).thenReturn(candidateDTOs);
    Response response = candidateController.getCandidatesInReview();
    assertEquals(200, response.getStatus());

    ControllerResponse controllerResponse = (ControllerResponse) response.getEntity();
    assertEquals("Candidates in review retrieved!", controllerResponse.message());
    assertEquals(candidateDTOs, controllerResponse.data());
  }

  @Test
  void testGetCurriculums() {

    List<CurriculumDTO> curriculumDTOs =
        List.of(
            new CurriculumDTO(
                "curriculum1",
                "part_time",
                "2025",
                "faculty1",
                Arrays.asList("discipline1", "discipline2")));

    when(candidateService.getCurriculums()).thenReturn(curriculumDTOs);
    Response response = candidateController.getCurriculums();
    assertEquals(200, response.getStatus());

    ControllerResponse controllerResponse = (ControllerResponse) response.getEntity();
    assertEquals("Curriculums retrieved", controllerResponse.message());
    assertEquals(curriculumDTOs, controllerResponse.data());
  }
}
