/* (C) @duquejo 2025 */
package com.duquejo.application.usecase;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.input.SetTokenUseCase;
import com.duquejo.domain.port.output.TokenRepositoryPort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class SetTokenUseCaseImpl implements SetTokenUseCase {

  private final TokenRepositoryPort tokenRepositoryPort;

  public SetTokenUseCaseImpl(TokenRepositoryPort tokenRepositoryPort) {
    this.tokenRepositoryPort = tokenRepositoryPort;
  }

  @Override
  public Uni<Token> setToken() {
    // Mocked external call
    Token token = new Token("Bearer", 10, 10, "ey12n3dqqwd.A4daw3QWD1dw54rqsqwd45a4sAdnhh00-0asd=");

    String tokenId = UUID.randomUUID().toString();

    return tokenRepositoryPort.set(tokenId, token).onItem().transform(unused -> token);
  }
}
