package com.github.mangila.webshop.backend.product.application;

import com.github.mangila.webshop.backend.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("it-test")
@SpringBootTest
@AutoConfigureWebTestClient
@Import(TestcontainersConfiguration.class)
class ProductCommandControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Insert one product and delete it afterwards")
    void insertAndDeleteProduct() {
        webTestClient.post()
                .uri("/products")
                .exchange()
                .expectStatus().isOk();
    }
}