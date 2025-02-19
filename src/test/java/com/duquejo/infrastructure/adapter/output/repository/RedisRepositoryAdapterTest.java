/* (C) @duquejo 2025 */
package com.duquejo.infrastructure.adapter.output.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.duquejo.domain.model.Token;
import com.duquejo.infrastructure.entity.TokenEntity;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class RedisRepositoryAdapterTest {

  @InjectMock
  ReactiveRedisDataSource reactive;

  // Inner dependencies
  @SuppressWarnings("unchecked")
  ReactiveValueCommands<String, TokenEntity> reactiveValueCommands =
      mock(ReactiveValueCommands.class);

  @SuppressWarnings("unchecked")
  ReactiveKeyCommands<String> reactiveKeyCommands = mock(ReactiveKeyCommands.class);

  @Inject
  private RedisRepositoryAdapter redisRepository;

  private final String key = "token-key";
  private final Token sampleToken = new Token("Bearer", 3600, 3600, "access_token");
  private final TokenEntity tokenEntity = new TokenEntity(key, sampleToken);

  @BeforeEach
  public void setUp() {
    when(reactive.value(TokenEntity.class)).thenReturn(reactiveValueCommands);
    when(reactive.key()).thenReturn(reactiveKeyCommands);
  }

  @Test
  void testRedisRepository_whenGet() {
    when(reactiveValueCommands.get(key)).thenReturn(Uni.createFrom().item(tokenEntity));

    TokenEntity result = redisRepository.get(key).await().indefinitely();

    assertNotNull(result);
    assertEquals(tokenEntity.getKey(), result.getKey());
    assertEquals(tokenEntity.getToken(), result.getToken());

    verify(reactiveValueCommands).get(key);
  }

  @Test
  void testRedisRepository_whenGetNull() {
    when(reactiveValueCommands.get(key)).thenReturn(Uni.createFrom().nullItem());

    TokenEntity result = redisRepository.get(key).await().indefinitely();

    assertNotNull(result, "If the <get> operation is null, must return a empty token instance");
  }

  @Test
  void testRedisRepository_whenSet() {
    when(reactiveValueCommands.set(anyString(), any(TokenEntity.class), any(SetArgs.class)))
        .thenReturn(Uni.createFrom().voidItem());

    Void set = redisRepository.set(key, sampleToken).await().indefinitely();

    assertNull(set, "The <set> operation must return null if success.");
  }
}
