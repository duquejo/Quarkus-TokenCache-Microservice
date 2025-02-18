package com.duquejo.application.usecase;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.input.GetTokenUseCase;
import com.duquejo.domain.port.output.TokenRepositoryPort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GetTokenUseCaseImpl implements GetTokenUseCase {

    private final TokenRepositoryPort tokenRepositoryPort;

    public GetTokenUseCaseImpl(TokenRepositoryPort tokenRepositoryPort) {
        this.tokenRepositoryPort = tokenRepositoryPort;
    }

    @Override
    public Uni<Token> getToken() {
        return tokenRepositoryPort.keys()
                .select().first().toUni()
                .flatMap(tokenRepositoryPort::get);
    }
}
