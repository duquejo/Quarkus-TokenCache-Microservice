/* (C) @duquejo 2025 */
package com.duquejo.domain.port.output;

import com.duquejo.domain.model.Token;
import com.duquejo.infrastructure.entity.TokenEntity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface TokenEntityRepositoryPort {
  Uni<TokenEntity> get(String key);

  Uni<Void> set(String key, Token value);

  Multi<String> keys();
}
