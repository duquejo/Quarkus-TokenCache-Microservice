/* (C) @duquejo 2025 */
package com.duquejo.infrastructure.adapter.output.repository;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.output.TokenEntityRepositoryPort;
import com.duquejo.infrastructure.entity.TokenEntity;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.quarkus.redis.datasource.value.SetArgs;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RedisRepositoryAdapter implements TokenEntityRepositoryPort {

  private static final Logger Log = Logger.getLogger(RedisRepositoryAdapter.class);

  private final ReactiveKeyCommands<String> keyCommands;
  private final ReactiveValueCommands<String, TokenEntity> valueCommands;

  public RedisRepositoryAdapter(ReactiveRedisDataSource reactive) {
    valueCommands = reactive.value(TokenEntity.class);
    keyCommands = reactive.key();
  }

  @Override
  public Uni<TokenEntity> get(String key) {
    return valueCommands
        .get(key)
        .onItem()
        .ifNull()
        .continueWith(TokenEntity::new)
        .onFailure()
        .invoke(throwable -> Log.errorf(
            "Error while getting token for the key '%s': '%s", key, throwable.getMessage()));
  }

  @Override
  public Uni<Void> set(String key, Token token) {
    SetArgs args = new SetArgs();

    TokenEntity tokenEntity = new TokenEntity(key, token);
    args.ex(Duration.ofSeconds(tokenEntity.getToken().expiresIn()));

    return valueCommands
        .set(tokenEntity.getKey(), tokenEntity, args)
        .replaceWithVoid()
        .onFailure()
        .invoke(throwable -> Log.errorf(
            "Error while saving token for the key '%s': '%s'",
            tokenEntity.getKey(), throwable.getMessage()));
  }

  @Override
  public Multi<String> keys() {
    return keyCommands
        .keys("*")
        .onItem()
        .transformToMulti(array -> Multi.createFrom().iterable(array))
        .onItem()
        .castTo(String.class)
        .onFailure()
        .invoke(throwable -> Log.error("Error getting keys list: ".concat(throwable.getMessage())));
  }
}
