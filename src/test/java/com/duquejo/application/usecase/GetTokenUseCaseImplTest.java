/* (C) @duquejo 2025 */
package com.duquejo.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.output.TokenEntityRepositoryPort;
import com.duquejo.infrastructure.entity.TokenEntity;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class GetTokenUseCaseImplTest {

  @InjectMock
  private TokenEntityRepositoryPort tokenEntityRepositoryPort;

  @Inject
  private GetTokenUseCaseImpl getTokenUseCase;

  private final String key = "valid-key";
  private final Token expectedToken = new Token("Bearer", 1000, 1000, "my-token-value");
  private final TokenEntity tokenEntity = new TokenEntity(key, expectedToken);

  @Test
  void testGetTokenUseCase_whenKeyExists() {
    when(tokenEntityRepositoryPort.keys()).thenReturn(Multi.createFrom().items(key));
    when(tokenEntityRepositoryPort.get(key)).thenReturn(Uni.createFrom().item(tokenEntity));

    Token result = getTokenUseCase.getToken().await().indefinitely();

    assertNotNull(result);
    assertEquals(expectedToken, result);

    verify(tokenEntityRepositoryPort).keys();
    verify(tokenEntityRepositoryPort).get(key);
  }

  @Test
  void testGetTokenUseCase__whenNoKeysPresent() {
    when(tokenEntityRepositoryPort.keys()).thenReturn(Multi.createFrom().empty());

    Token result = getTokenUseCase.getToken().await().indefinitely();

    assertNull(result);

    verify(tokenEntityRepositoryPort, never()).get(anyString());
  }
}
