package com.tuvarna.phd.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TestContats {
  @Test
  void testGetContacts() {
    given().when().get("/contacts").then().statusCode(200).body(is("Teachers"));
  }
}