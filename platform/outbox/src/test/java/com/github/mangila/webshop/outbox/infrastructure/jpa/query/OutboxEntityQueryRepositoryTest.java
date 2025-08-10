package com.github.mangila.webshop.outbox.infrastructure.jpa.query;

import com.github.mangila.webshop.outbox.OutboxTestContainerConfig;
import com.github.mangila.webshop.outbox.infrastructure.jpa.outbox.OutboxEntityQueryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({OutboxTestContainerConfig.class})
class OutboxEntityQueryRepositoryTest {

    @Autowired
    private OutboxEntityQueryRepository repository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void replay() {
        repository.findById(1L);
    }

    @Test
    void findAllIdsByDomainAndStatus() {
    }

    @Test
    void findAllIdsByStatusAndDateBefore() {
    }
}