package com.github.mangila.webshop;

import org.springframework.boot.SpringApplication;

public class TestWebshopApplication {

	public static void main(String[] args) {
		SpringApplication.from(Application::main)
				.with(TestcontainersConfiguration.class)
				.run(args);
	}

}
