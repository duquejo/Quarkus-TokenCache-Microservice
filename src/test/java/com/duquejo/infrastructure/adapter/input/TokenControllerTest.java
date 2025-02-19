/* (C) @duquejo 2025 */
package com.duquejo.infrastructure.adapter.input;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.duquejo.application.service.TokenService;
import com.duquejo.domain.model.Token;
import com.duquejo.utils.InMemoryLogHandler;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import java.net.URL;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(TokenController.class)
class TokenControllerTest {

  @TestHTTPResource("/token")
  URL tokenEndpoint;

  @InjectMock
  TokenService tokenService;

  Token mockedToken = new Token("tokenType", 123, 123, "access_token");

  @Test
  void testGetTokenController_whenSuccess() {
    when(tokenService.getOrSetToken()).thenReturn(Uni.createFrom().item(mockedToken));

    given()
        .when()
        .get(tokenEndpoint)
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .header("Content-Type", containsString(ContentType.JSON.toString()))
        .header("Content-Length", notNullValue())
        .body("token.token_type", instanceOf(String.class))
        .body("token.expires_in", instanceOf(Integer.class))
        .body("token.ext_expires_in", instanceOf(Integer.class))
        .body("token.access_token", instanceOf(String.class))
        .body("timestamp", instanceOf(String.class));

    verify(tokenService).getOrSetToken();
  }

  @Test
  void testGetTokenController_whenTokenFailureWithFallback() {
    final String expectedLogMessage = "Error while getting token";
    final String simulatedError = "Something happened";

    InMemoryLogHandler inMemoryLogHandler = getInMemoryLogHandler(expectedLogMessage);
    when(tokenService.getOrSetToken())
        .thenReturn(Uni.createFrom().failure(new RuntimeException((simulatedError))));

    given()
        .when()
        .get(tokenEndpoint)
        .then()
        .statusCode(Response.Status.TOO_MANY_REQUESTS.getStatusCode())
        .body("message", equalTo("Token retrieval retries exceeded, try later."))
        .body("status", equalTo(Response.Status.TOO_MANY_REQUESTS.getReasonPhrase()))
        .body("timestamp", instanceOf(String.class));

    List<LogRecord> logRecords = inMemoryLogHandler.getRecords();
    assertEquals(1, logRecords.size());
    assertTrue(logRecords.getFirst().getMessage().contains(expectedLogMessage));
  }

  private static InMemoryLogHandler getInMemoryLogHandler(String expectedLogMessage) {
    InMemoryLogHandler inMemoryLogHandler =
        new InMemoryLogHandler(rec -> rec.getMessage().contains(expectedLogMessage));
    LogManager.getLogManager().getLogger("").addHandler(inMemoryLogHandler);
    return inMemoryLogHandler;
  }
}
