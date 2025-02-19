/* (C) @duquejo 2025 */
package com.duquejo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TokenControllerTest {
  @Test
  void testGetController() {
    given()
        .when()
        .get("/token")
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("token.token_type", instanceOf(String.class))
        .body("token.expires_in", instanceOf(Integer.class))
        .body("token.ext_expires_in", instanceOf(Integer.class))
        .body("token.access_token", instanceOf(String.class))
        .body("timestamp", instanceOf(String.class));
  }
}
