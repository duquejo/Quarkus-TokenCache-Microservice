package com.duquejo.application.service;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.input.GetTokenUseCase;
import com.duquejo.domain.port.input.SetTokenUseCase;
import io.quarkus.logging.Log;
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

    public Uni<Token> getOrSetToken() {
        return getTokenUseCase.getToken()
                .onItem().ifNotNull().invoke(oldToken ->
                        Log.warnf("Available token '%s'", oldToken))
                .onItem().ifNull().switchTo(() ->
                    setTokenUseCase.setToken().invoke(newToken ->
                        Log.warnf("Generated token '%s'", newToken)))
                .onFailure()
                    .invoke(ex ->
                        Log.error("Error while token processing", ex));
    }

    public Uni<Token> setToken() {
        return setTokenUseCase.setToken();
    }
}
