package com.github.mangila.webshop.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.github.mangila.webshop")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}