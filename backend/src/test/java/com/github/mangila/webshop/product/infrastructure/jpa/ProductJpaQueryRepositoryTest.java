package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.TestPostgresContainer;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.infrastructure.config.JpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("it-test")
@DataJpaTest
@Import({
        ProductJpaQueryRepository.class,
        ProductEntityMapper.class,
        TestPostgresContainer.class,
        JpaConfig.class
})
class ProductJpaQueryRepositoryTest {

    @Autowired
    private ProductJpaQueryRepository repository;

    @Test
    void findByIdOrThrow() {
        var l = repository.existsById(new ProductId(UUID.randomUUID()));
        assertFalse(l);
    }
}