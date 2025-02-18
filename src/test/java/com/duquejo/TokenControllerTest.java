package com.duquejo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class TokenControllerTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/token")
          .then()
             .statusCode(200)
             .body(is(anything()));
    }

}