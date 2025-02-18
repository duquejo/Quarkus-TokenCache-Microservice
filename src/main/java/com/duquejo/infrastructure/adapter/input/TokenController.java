package com.duquejo.infrastructure.adapter.input;

import com.duquejo.application.service.TokenService;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
public class TokenController {

    private static final Logger Log = Logger.getLogger(TokenController.class);

    private final TokenService service;

    public TokenController(TokenService service) {
        this.service = service;
    }

    @GET
    public Uni<Response> getToken() {
        return service.getOrSetToken()
                .map(token -> {

                    HashMap<String, Object> response = new HashMap<>();

                    response.put("token", token);
                    response.put("timestamp", LocalDateTime.now());

                    return Response.ok(response).build();

                })
                .onFailure()
                    .invoke(ex -> Log.error("Error while getting token: ".concat(ex.getMessage())))
                .onFailure()
                    .recoverWithItem( throwable ->
                            Response.status(Response.Status.BAD_REQUEST)
                            .entity(throwable.getMessage())
                            .build()
                    );
    }
}
