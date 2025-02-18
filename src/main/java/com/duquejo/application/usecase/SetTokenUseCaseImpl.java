package com.duquejo.application.usecase;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.input.SetTokenUseCase;
import com.duquejo.domain.port.output.TokenRepositoryPort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

@ApplicationScoped
public class SetTokenUseCaseImpl implements SetTokenUseCase {

    private final TokenRepositoryPort tokenRepositoryPort;

    public SetTokenUseCaseImpl(TokenRepositoryPort tokenRepositoryPort) {
        this.tokenRepositoryPort = tokenRepositoryPort;
    }

    @Override
    public Uni<Token> setToken(Token token) {
        return tokenRepositoryPort.set(LocalDateTime.now().toString(), token)
            .onItem()
            .transform(unused -> token);
    }
}
