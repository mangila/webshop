package com.github.mangila.webshop.relay;

import org.springframework.boot.SpringApplication;

public class TestRelayApplication {

	public static void main(String[] args) {
		SpringApplication.from(RelayApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
