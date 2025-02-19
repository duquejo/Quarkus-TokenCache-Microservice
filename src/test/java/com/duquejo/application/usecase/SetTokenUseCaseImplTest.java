/* (C) @duquejo 2025 */
package com.duquejo.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.output.TokenClientPort;
import com.duquejo.domain.port.output.TokenEntityRepositoryPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SetTokenUseCaseImplTest {

  @InjectMock
  private TokenEntityRepositoryPort tokenRepository;

  @InjectMock
  @RestClient
  private TokenClientPort tokenClientPort;

  @Inject
  private SetTokenUseCaseImpl setTokenUseCase;

  private final Token expectedToken = new Token("tokenType", 1234, 1234, "abc123");

  @Test
  void testSetTokenUseCase_whenSuccess() {
    Uni<Token> asyncToken = Uni.createFrom().item(expectedToken);

    when(tokenClientPort.getTokenAsync()).thenReturn(asyncToken);
    when(tokenRepository.set(anyString(), eq(expectedToken)))
        .thenReturn(Uni.createFrom().voidItem());

    Token result = setTokenUseCase.setToken().await().indefinitely();

    assertNotNull(result);
    assertEquals(expectedToken, result);

    verify(tokenRepository).set(anyString(), eq(expectedToken));
  }
}
