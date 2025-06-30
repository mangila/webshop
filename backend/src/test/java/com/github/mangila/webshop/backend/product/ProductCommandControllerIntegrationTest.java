package com.github.mangila.webshop.backend.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.TestcontainersConfiguration;
import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.event.model.EventTopic;
import com.github.mangila.webshop.backend.product.domain.command.ProductUpsertCommand;
import com.github.mangila.webshop.backend.product.domain.ProductEventType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestcontainersConfiguration.class)
class ProductCommandControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void beforeEach() {
        webTestClient.post()
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_UPSERT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ProductTestUtil.dummyProductUpsertCommand())
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody();
    }

    @AfterEach
    void afterEach() {
        webTestClient.method(org.springframework.http.HttpMethod.DELETE)
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_DELETE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ProductTestUtil.dummyProductDeleteCommand())
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody();
    }

    @Test
    @DisplayName("Should upsert a existing Product when ProductUpsertCommand  is executed")
    void shouldUpsertExistingProductWhenProductUpsertCommandIsExecuted() {
        var command = new ProductUpsertCommand(ProductTestUtil.dummyProductId(),
                "Dummy Product Name 2",
                new BigDecimal("21.99"),
                "{}");
        var event = webTestClient.post()
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_UPSERT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody(Event.class)
                .returnResult()
                .getResponseBody();

        assertThat(event).isNotNull();
        assertThat(event.topic()).isEqualTo(EventTopic.PRODUCT);
        assertThat(event.type()).isEqualTo(ProductEventType.PRODUCT_UPSERTED.name());
        assertThat(event.aggregateId()).isEqualTo("dummy-product-id");

        JsonNode data = event.data();
        assertThat(data).isNotNull();
        assertThat(data.get("id").asText()).isEqualTo("dummy-product-id");
        assertThat(data.get("name").asText()).isEqualTo("Dummy Product Name 2");
        assertThat(data.get("price").asDouble()).isEqualTo(21.99);
        assertThat(data.get("attributes")).isEmpty();
    }
}
