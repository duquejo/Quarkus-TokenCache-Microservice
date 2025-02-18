package com.duquejo.application.service;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.input.GetTokenUseCase;
import com.duquejo.domain.port.input.SetTokenUseCase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {

    private final GetTokenUseCase getTokenUseCase;
    private final SetTokenUseCase setTokenUseCase;

    public TokenService(GetTokenUseCase getTokenUseCase, SetTokenUseCase setTokenUseCase) {
        this.getTokenUseCase = getTokenUseCase;
        this.setTokenUseCase = setTokenUseCase;
    }


    public Uni<Token> getToken() {
        return getTokenUseCase.getToken();
    }

    public Uni<Token> setToken(Token token) {
        return setTokenUseCase.setToken(token);
    }
}
