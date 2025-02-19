/* (C) @duquejo 2025 */
package com.duquejo.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.duquejo.application.usecase.GetTokenUseCaseImpl;
import com.duquejo.application.usecase.SetTokenUseCaseImpl;
import com.duquejo.domain.model.Token;
import com.duquejo.utils.InMemoryLogHandler;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.UniAwait;
import jakarta.inject.Inject;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TokenServiceTest {

  @InjectMock
  private GetTokenUseCaseImpl getTokenUseCase;

  @InjectMock
  private SetTokenUseCaseImpl setTokenUseCase;

  @Inject
  private TokenService tokenService;

  private final Token expectedToken = new Token("tokenType", 123, 123, "access_token");

  @Test
  void testGetOrSetToken__whenAvailableToken() {
    final String expectedLogMessage = "Available token";
    InMemoryLogHandler inMemoryLogHandler = getInMemoryLogHandler(expectedLogMessage);

    when(getTokenUseCase.getToken()).thenReturn(Uni.createFrom().item(expectedToken));

    Token result = tokenService.getOrSetToken().await().indefinitely();
    List<LogRecord> logRecords = inMemoryLogHandler.getRecords();

    assertNotNull(result);
    assertEquals(expectedToken, result);
    assertEquals(1, logRecords.size());
    assertTrue(logRecords.getFirst().getMessage().contains(expectedLogMessage));

    verify(getTokenUseCase).getToken();
    verify(setTokenUseCase, never()).setToken();
  }

  @Test
  void testGetOrSetToken__whenGenerateToken() {
    final String expectedLogMessage = "Generated token";
    InMemoryLogHandler inMemoryLogHandler = getInMemoryLogHandler(expectedLogMessage);

    when(getTokenUseCase.getToken()).thenReturn(Uni.createFrom().nullItem());
    when(setTokenUseCase.setToken()).thenReturn(Uni.createFrom().item(expectedToken));

    Token result = tokenService.getOrSetToken().await().indefinitely();
    List<LogRecord> logRecords = inMemoryLogHandler.getRecords();

    assertNotNull(result);
    assertEquals(expectedToken, result);
    assertEquals(1, logRecords.size());
    assertTrue(logRecords.getFirst().getMessage().contains(expectedLogMessage));

    verify(getTokenUseCase).getToken();
    verify(setTokenUseCase).setToken();
  }

  @Test
  void testGetOrSetToken__whenFailure() {
    final String expectedLogMessage = "Error while token processing";
    final String expectedError = "Something happened";
    InMemoryLogHandler inMemoryLogHandler = getInMemoryLogHandler(expectedLogMessage);

    when(getTokenUseCase.getToken())
        .thenReturn(Uni.createFrom().failure(new RuntimeException(expectedError)));

    UniAwait<Token> uniResult = tokenService.getOrSetToken().await();

    RuntimeException exception = assertThrows(RuntimeException.class, uniResult::indefinitely);
    List<LogRecord> logRecords = inMemoryLogHandler.getRecords();

    assertEquals(expectedError, exception.getMessage());
    assertEquals(1, logRecords.size());
    assertTrue(logRecords.getFirst().getMessage().contains(expectedLogMessage));

    verify(getTokenUseCase).getToken();
    verify(setTokenUseCase, never()).setToken();
  }

  private static InMemoryLogHandler getInMemoryLogHandler(String expectedLogMessage) {
    InMemoryLogHandler inMemoryLogHandler =
        new InMemoryLogHandler(rec -> rec.getMessage().contains(expectedLogMessage));
    LogManager.getLogManager().getLogger("").addHandler(inMemoryLogHandler);
    return inMemoryLogHandler;
  }
}
