package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.TestCacheConfig;
import com.github.mangila.webshop.product.application.gateway.ProductMapperGateway;
import com.github.mangila.webshop.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.shared.infrastructure.config.CacheConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = {
        ProductQueryService.class,
        CacheManager.class,
})
@Import({TestCacheConfig.class,
        CacheConfig.class})
@MockitoBean(types = {
        ProductMapperGateway.class,
        ProductRepositoryGateway.class
})
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