package com.github.mangila.webshop.backend.product;

import com.github.mangila.webshop.backend.TestcontainersConfiguration;
import com.github.mangila.webshop.backend.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.command.model.ProductUpsertCommand;
import com.github.mangila.webshop.backend.product.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestcontainersConfiguration.class)
class ProductCommandControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Should create a new product when upsert command is executed")
    void shouldCreateNewProductWhenUpsertCommandIsExecuted() {
        // Given
        ProductUpsertCommand command = new ProductUpsertCommand(
                "testproduct1",
                "Test Product 1",
                new BigDecimal("19.99"),
                // language=JSON
                "{\"color\":\"red\",\"size\":\"medium\"}"
        );

        // When & Then
        Product product = webTestClient.post().uri("/api/v1/product/command/upsert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Product.class)
                .returnResult()
                .getResponseBody();

        assertThat(product).isNotNull();
        assertThat(product.id()).isEqualTo("testproduct1");
        assertThat(product.name()).isEqualTo("Test Product 1");
        assertThat(product.price()).isEqualTo(new BigDecimal("19.99"));
        assertThat(product.attributes().get("color").asText()).isEqualTo("red");
        assertThat(product.attributes().get("size").asText()).isEqualTo("medium");
    }

    @Test
    @DisplayName("Should update an existing product when upsert command is executed")
    void shouldUpdateExistingProductWhenUpsertCommandIsExecuted() {
        // Given
        ProductUpsertCommand createCommand = new ProductUpsertCommand(
                "testproduct2",
                "Test Product 2",
                new BigDecimal("29.99"),
                // language=JSON
                "{\"color\":\"blue\",\"size\":\"large\"}"
        );

        // Create the product first
        webTestClient.post()
                .uri("/api/v1/product/command/upsert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createCommand)
                .exchange()
                .expectStatus()
                .isOk();

        // Update command
        ProductUpsertCommand updateCommand = new ProductUpsertCommand(
                "testproduct2",
                "Updated Test Product 2",
                new BigDecimal("39.99"),
                // language=JSON
                "{\"color\":\"green\",\"size\":\"small\"}"
        );

        // When & Then
        Product product = webTestClient.post().uri("/api/v1/product/command/upsert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateCommand)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Product.class)
                .returnResult()
                .getResponseBody();

        assertThat(product).isNotNull();
        assertThat(product.id()).isEqualTo("testproduct2");
        assertThat(product.name()).isEqualTo("Updated Test Product 2");
        assertThat(product.price()).isEqualTo(new BigDecimal("39.99"));
        assertThat(product.attributes().get("color").asText()).isEqualTo("green");
        assertThat(product.attributes().get("size").asText()).isEqualTo("small");
    }

    @Test
    @DisplayName("Should delete an existing product when delete command is executed")
    void shouldDeleteExistingProductWhenDeleteCommandIsExecuted() {
        // Given
        ProductUpsertCommand createCommand = new ProductUpsertCommand(
                "testproduct3",
                "Test Product 3",
                new BigDecimal("49.99"),
                // language=JSON
                "{\"color\":\"yellow\",\"size\":\"xlarge\"}"
        );

        // Create the product first
        webTestClient.post().uri("/api/v1/product/command/upsert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createCommand)
                .exchange()
                .expectStatus().isOk();

        // Delete command
        ProductDeleteCommand deleteCommand = new ProductDeleteCommand("testproduct3");

        // When & Then
        Product product = webTestClient.method(org.springframework.http.HttpMethod.DELETE)
                .uri("/api/v1/product/command/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(deleteCommand)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .returnResult()
                .getResponseBody();

        assertThat(product).isNotNull();
        assertThat(product.id()).isEqualTo("testproduct3");
    }
}
