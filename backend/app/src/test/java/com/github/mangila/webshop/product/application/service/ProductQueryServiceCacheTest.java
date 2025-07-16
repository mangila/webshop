package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.TestCacheConfig;
import com.github.mangila.webshop.TestPostgresContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import({TestPostgresContainer.class, TestCacheConfig.class})
class ProductQueryServiceCacheTest {

    @Autowired
    private ProductQueryService service;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByIdOrThrow() {
        cacheManager.getCacheNames();
    }

    @Test
    void existsById() {
    }
}