package com.github.mangila.webshop.event.command;

import com.github.mangila.webshop.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class EventCommandServiceTest {

    @Autowired
    private EventCommandService service;

    @Test
    void emit() {

    }
}