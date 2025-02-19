/* (C) @duquejo 2025 */
package com.duquejo.infrastructure.adapter.input;

import com.duquejo.application.service.TokenService;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Token", description = "Token generation services")
public class TokenController {

  private static final Logger Log = Logger.getLogger(TokenController.class);
  private final TokenService service;

  public TokenController(TokenService service) {
    this.service = service;
  }

  @GET
  @Schema(name = "Get token", description = "Generate or retrieve signed token")
  @CircuitBreaker(
      requestVolumeThreshold = 4,
      delay = 1000,
      successThreshold = 2,
      failOn = {ClientWebApplicationException.class})
  @Fallback(fallbackMethod = "fallbackFromSource")
  public Uni<Response> getToken() {
    return service
        .getOrSetToken()
        .map(token -> {
          HashMap<String, Object> response = new HashMap<>();
          response.put("token", token);
          response.put("timestamp", LocalDateTime.now());
          return Response.ok(response).build();
        })
        .onFailure()
        .invoke(ex -> Log.error("Error while getting token: ".concat(ex.getMessage())))
        .onFailure()
        .recoverWithItem(throwable -> Response.status(Response.Status.BAD_REQUEST)
            .entity(throwable.getMessage())
            .build());
  }

  public Uni<Response> fallbackFromSource() {
    HashMap<String, Object> response = new HashMap<>();
    response.put("status", Response.Status.TOO_MANY_REQUESTS.getReasonPhrase());
    response.put("message", "Token retrieval retries exceeded, try later.");
    response.put("timestamp", LocalDateTime.now());
    return Uni.createFrom()
        .item(Response.status(Response.Status.TOO_MANY_REQUESTS.getStatusCode())
            .entity(response)
            .build());
  }
}
