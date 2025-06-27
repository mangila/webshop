package com.github.mangila.webshop.backend.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.TestcontainersConfiguration;
import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.event.model.EventTopic;
import com.github.mangila.webshop.backend.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.command.model.ProductUpsertCommand;
import com.github.mangila.webshop.backend.product.model.ProductEventType;
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
    @DisplayName("Should create a new product when ProductInsertCommand command is executed")
    void shouldCreateANewProductWhenProductInsertCommandIsExecuted() {
        // Given
        ProductUpsertCommand command = new ProductUpsertCommand(
                "testproduct1",
                "Test Product 1",
                new BigDecimal("19.99"),
                // language=JSON
                "{\"color\":\"red\",\"size\":\"medium\"}"
        );

        // When & Then
        Event event = webTestClient.post()
                .uri("/api/v1/product/command/upsert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Event.class)
                .returnResult()
                .getResponseBody();

        assertThat(event).isNotNull();
        assertThat(event.topic()).isEqualTo(EventTopic.PRODUCT);
        assertThat(event.type()).isEqualTo(ProductEventType.PRODUCT_INSERTED.name());
        assertThat(event.aggregateId()).isEqualTo("testproduct1");

        JsonNode data = event.data();
        assertThat(data).isNotNull();
        assertThat(data.isEmpty()).isFalse();
        assertThat(data.isNull()).isFalse();
        assertThat(data.get("id").asText()).isEqualTo("testproduct1");
        assertThat(data.get("name").asText()).isEqualTo("Test Product 1");
        assertThat(data.get("price").asDouble()).isEqualTo(19.99);
        assertThat(data.get("attributes").get("color").asText()).isEqualTo("red");
        assertThat(data.get("attributes").get("size").asText()).isEqualTo("medium");
    }

    @Test
    @DisplayName("Should delete an existing product when ProductDeleteCommand is executed")
    void shouldDeleteExistingProductWhenProductDeleteCommandIsExecuted() {
        // Given
        ProductUpsertCommand insertCommand = new ProductUpsertCommand(
                "testproduct3",
                "Test Product 3",
                new BigDecimal("49.99"),
                // language=JSON
                "{\"color\":\"yellow\",\"size\":\"xlarge\"}"
        );

        // Create the product first
        webTestClient.post().uri("/api/v1/product/command/upsert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(insertCommand)
                .exchange()
                .expectStatus().isOk();

        // Delete command
        ProductDeleteCommand deleteCommand = new ProductDeleteCommand("testproduct3");

        // When & Then
        Event event = webTestClient.method(org.springframework.http.HttpMethod.DELETE)
                .uri("/api/v1/product/command/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(deleteCommand)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Event.class)
                .returnResult()
                .getResponseBody();

        assertThat(event).isNotNull();
        assertThat(event.aggregateId()).isEqualTo("testproduct3");
    }
}
