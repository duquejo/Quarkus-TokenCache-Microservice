package com.duquejo.domain.port.input;

import com.duquejo.domain.model.Token;
import io.smallrye.mutiny.Uni;

public interface SetTokenUseCase {
    Uni<Token> setToken();
}
