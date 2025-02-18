package com.duquejo.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Token(
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") Integer expiresIn,
        @JsonProperty("ext_expires_in") Integer extExpiresIn,
        @JsonProperty("access_token") String accessToken) {
}
