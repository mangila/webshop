package com.github.mangila.webshop.backend;

import org.springframework.boot.SpringApplication;

public class TestWebshopApplication {

	public static void main(String[] args) {
		SpringApplication.from(Application::main)
				.with(TestcontainersConfiguration.class)
				.run(args);
	}

}
