package com.github.mangila.webshop.outbox.infrastructure.jpa.command;

import com.github.mangila.webshop.outbox.OutboxTestContainerConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({OutboxTestContainerConfig.class})
class OutboxEntityCommandRepositoryTest {

    @Autowired
    private OutboxEntityCommandRepository repository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByIdForUpdate() {
        repository.findByIdForUpdate(1L);
    }

    @Test
    void updateStatus() {
    }
}