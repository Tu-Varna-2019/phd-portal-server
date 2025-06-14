package com.tuvarna.phd.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

import com.tuvarna.phd.dto.CandidateApplyDTO;
import com.tuvarna.phd.dto.CurriculumCreateDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
// import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(CandidateController.class)
public class TestCandidateController {

  @InjectMock CandidateController candidateController;
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
    // when(candidateController.apply(candidateApplyDTO))
    //     .thenReturn(
    //         Response.ok()
    //             .entity(new ControllerResponse("Candidate application finished successfully!"))
    //             .build());

    given()
        .contentType(ContentType.JSON)
        .body(candidateApplyDTO)
        .when()
        .post("/apply")
        .then()
        .statusCode(200)
        .body("message", is("Candidate application finished successfully!"));

    // verify(candidateController).apply(candidateApplyDTO);
  }
}
