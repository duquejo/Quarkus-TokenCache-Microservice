/* (C) @duquejo 2025 */
package com.duquejo.domain.port.output;

import com.duquejo.domain.model.Token;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "token-client")
public interface TokenClientPort {
  @GET
  Uni<Token> getTokenAsync();
}
