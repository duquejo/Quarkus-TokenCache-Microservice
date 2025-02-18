/* (C) @duquejo 2025 */
package com.duquejo.infrastructure.entity;

import com.duquejo.domain.model.Token;

public class TokenEntity {
  private String key;
  private Token token;

  public TokenEntity() {}

  public TokenEntity(String key, Token token) {
    this.key = key;
    this.token = token;
  }

  public String getKey() {
    return key;
  }

  public Token getToken() {
    return token;
  }
}
