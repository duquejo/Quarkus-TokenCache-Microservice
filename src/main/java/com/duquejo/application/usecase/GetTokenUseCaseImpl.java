/* (C) @duquejo 2025 */
package com.duquejo.application.usecase;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.input.GetTokenUseCase;
import com.duquejo.domain.port.output.TokenEntityRepositoryPort;
import com.duquejo.infrastructure.entity.TokenEntity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GetTokenUseCaseImpl implements GetTokenUseCase {

  private final TokenEntityRepositoryPort tokenEntityRepositoryPort;

  public GetTokenUseCaseImpl(TokenEntityRepositoryPort tokenEntityRepositoryPort) {
    this.tokenEntityRepositoryPort = tokenEntityRepositoryPort;
  }

  @Override
  public Uni<Token> getToken() {
    return tokenEntityRepositoryPort.keys().select().first().toUni().flatMap(key -> {
      if (key == null) {
        return Uni.createFrom().nullItem();
      }
      return tokenEntityRepositoryPort.get(key).map(TokenEntity::getToken);
    });
  }
}
