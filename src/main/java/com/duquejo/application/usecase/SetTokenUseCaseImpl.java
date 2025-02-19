/* (C) @duquejo 2025 */
package com.duquejo.application.usecase;

import com.duquejo.domain.model.Token;
import com.duquejo.domain.port.input.SetTokenUseCase;
import com.duquejo.domain.port.output.TokenClientPort;
import com.duquejo.domain.port.output.TokenEntityRepositoryPort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class SetTokenUseCaseImpl implements SetTokenUseCase {

  private final TokenEntityRepositoryPort tokenRepository;
  private final TokenClientPort tokenClient;

  public SetTokenUseCaseImpl(
      TokenEntityRepositoryPort tokenRepository, @RestClient TokenClientPort tokenClient) {
    this.tokenRepository = tokenRepository;
    this.tokenClient = tokenClient;
  }

  @Override
  public Uni<Token> setToken() {
    return tokenClient.getTokenAsync().call(token -> tokenRepository
        .set(UUID.randomUUID().toString(), token)
        .onItem()
        .transform(unused -> token));
  }
}
