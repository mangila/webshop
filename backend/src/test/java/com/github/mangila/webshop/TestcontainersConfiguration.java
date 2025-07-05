package com.github.mangila.webshop;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.5-alpine"));
    }

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitMQContainer() {
        var container = new RabbitMQContainer(DockerImageName.parse("rabbitmq:4.1.1-alpine"))
                .withAdminUser("my_user")
                .withAdminPassword("secret")
                .withPluginsEnabled("rabbitmq_stream");
        container.setPortBindings(List.of("5672:5672", "5552:5552"));
        return container;
    }
}