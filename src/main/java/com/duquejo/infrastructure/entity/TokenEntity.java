package com.duquejo.infrastructure.entity;

import com.duquejo.domain.model.Token;

public class TokenEntity {
    public String key;
    public Token token;

    public TokenEntity() {
    }

    public TokenEntity(String key, Token token) {
        this.key = key;
        this.token = token;
    }
}
