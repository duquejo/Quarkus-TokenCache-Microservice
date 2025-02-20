package com.duquejo.infrastructure.adapter.output.repository;

import com.duquejo.domain.model.Token;
import com.duquejo.infrastructure.entity.TokenEntity;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedisRepositoryAdapterTest {

    @Inject
    private RedisRepositoryAdapter redisRepository;

    private final String expectedKey = "token-key";
    private final Token expectedToken = new Token("Bearer", 3600, 3600, "access_token");

    @Test
    @Order(1)
    void testRedisRepository_whenGetNull() {
        TokenEntity result = redisRepository.get(expectedKey).await().indefinitely();

        assertNotNull(result);
        assertNull(result.getKey());
        assertNull(result.getToken());
    }

    @Test
    @Order(2)
    void testRedisRepository_whenGet() {
        redisRepository.set(expectedKey, expectedToken).await().indefinitely();

        TokenEntity result = redisRepository.get(expectedKey).await().indefinitely();

        assertNotNull(result);
        assertEquals(expectedKey, result.getKey());
        assertEquals(expectedToken, result.getToken());
    }

    @Test
    @Order(3)
    void testRedisRepository_whenSet() {
        Void result = redisRepository.set(expectedKey, expectedToken).await().indefinitely();

        assertNull(result, "If the <get> operation is null, must return a empty token instance");
    }

    @Test
    @Order(4)
    void testRedisRepository_whenKeys() {
        redisRepository.set(expectedKey, expectedToken).await().indefinitely();

        List<String> keysList = redisRepository.keys().collect().asList().await().indefinitely();

        assertEquals(1, keysList.size());
        assertEquals(expectedKey, keysList.getFirst());
    }
}
