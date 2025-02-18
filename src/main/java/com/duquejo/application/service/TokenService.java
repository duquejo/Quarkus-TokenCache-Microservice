/* (C) @duquejo 2025 */
package com.duquejo.application.service;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.input.GetTokenUseCase;
import com.duquejo.domain.port.input.SetTokenUseCase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TokenService {

  private static final Logger Log = Logger.getLogger(TokenService.class);

  private final GetTokenUseCase getTokenUseCase;
  private final SetTokenUseCase setTokenUseCase;

  public TokenService(GetTokenUseCase getTokenUseCase, SetTokenUseCase setTokenUseCase) {
    this.getTokenUseCase = getTokenUseCase;
    this.setTokenUseCase = setTokenUseCase;
  }

  public Uni<Token> getOrSetToken() {
    return getTokenUseCase
        .getToken()
        .onItem()
        .ifNotNull()
        .invoke(oldToken -> Log.infof("Available token '%s'", oldToken))
        .onItem()
        .ifNull()
        .switchTo(() -> setTokenUseCase
            .setToken()
            .invoke(newToken -> Log.warnf("Generated token '%s'", newToken)))
        .onFailure()
        .invoke(ex -> Log.error("Error while token processing", ex));
  }
}
