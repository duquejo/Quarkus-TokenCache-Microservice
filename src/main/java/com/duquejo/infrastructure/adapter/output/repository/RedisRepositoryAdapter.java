package com.duquejo.infrastructure.adapter.output.repository;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.output.TokenRepositoryPort;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.quarkus.redis.datasource.value.SetArgs;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.time.Duration;

@ApplicationScoped
public class RedisRepositoryAdapter implements TokenRepositoryPort {

    private static final Logger Log =  Logger.getLogger(RedisRepositoryAdapter.class);

    private final ReactiveKeyCommands<String> keyCommands;
    private final ReactiveValueCommands<String, Token> valueCommands;

    public RedisRepositoryAdapter(ReactiveRedisDataSource reactive) {
        valueCommands = reactive.value(Token.class);
        keyCommands = reactive.key();
    }

    @Override
    public Uni<Token> get(String key) {
        return valueCommands.get(key)
                .onItem()
                .ifNull().continueWith(Token::new)
                .onFailure().invoke(throwable ->
                    Log.errorf("Error while getting token for the key '%s': '%s", key, throwable.getMessage()));
    }

    @Override
    public Uni<Void> set(String key, Token value) {
        SetArgs args = new SetArgs();
        args.ex(Duration.ofSeconds(5));
        return valueCommands.set(key, value, args)
                .replaceWithVoid()
                .onFailure().invoke(throwable ->
                    Log.errorf("Error while saving token for the key '%s': '%s'", key, throwable.getMessage()));
    }

    @Override
    public Uni<Void> del(String key) {
        return keyCommands.del(key)
                .replaceWithVoid()
                .onFailure().invoke(throwable ->
                    Log.errorf("Error while deleting token for the key '%s': '%s'", key, throwable.getMessage()));
    }

    @Override
    public Multi<String> keys() {
        return keyCommands.keys("*")
                .onItem().transformToMulti(array -> Multi.createFrom().iterable(array))
                .onItem().castTo(String.class)
                .onFailure().invoke(throwable ->
                    Log.error("Error getting keys list: ".concat(throwable.getMessage())));
    }
}
