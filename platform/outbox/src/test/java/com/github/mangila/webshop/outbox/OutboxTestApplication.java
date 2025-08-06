package com.github.mangila.webshop.outbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.github.mangila.webshop.outbox")
public class OutboxTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutboxTestApplication.class, args);
    }
}
