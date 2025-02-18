package com.duquejo.domain.port.output;

import com.duquejo.domain.model.Token;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface TokenRepositoryPort {
    Uni<Token> get(String key);

    Uni<Void> set(String key, Token value);

    Multi<String> keys();
}
