package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.TestPostgresContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestPostgresContainer.class)
class ProductJpaCommandRepositoryTest {

    @Autowired
    private ProductJpaCommandRepository repository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void insert() {
    }

    @Test
    void deleteById() {
    }
}