package com.github.mangila.webshop;

import org.springframework.boot.SpringApplication;

public class TestWebshopApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main)
                .with(TestPostgresContainer.class)
                .with(TestRabbitMqContainer.class)
                .with(TestCacheConfig.class)
                .run(args);
    }

}
