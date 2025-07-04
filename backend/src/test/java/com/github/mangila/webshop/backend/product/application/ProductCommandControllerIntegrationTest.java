package com.github.mangila.webshop.backend.product.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.backend.TestcontainersConfiguration;
import com.github.mangila.webshop.backend.outboxevent.domain.OutboxEvent;
import com.github.mangila.webshop.backend.product.domain.command.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.domain.command.ProductInsertCommand;
import com.github.mangila.webshop.backend.product.domain.model.ProductId;
import com.github.mangila.webshop.backend.product.domain.model.ProductName;
import com.github.mangila.webshop.backend.product.domain.model.ProductPrice;
import com.github.mangila.webshop.backend.product.domain.model.ProductUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@ActiveProfiles("it-test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestcontainersConfiguration.class})
class ProductCommandControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Insert one product and delete it afterwards")
    void insertAndDeleteProduct() {
        OutboxEvent outboxEvent = webTestClient.post()
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_INSERT)
                .bodyValue(new ProductInsertCommand(
                        new ProductName("name"),
                        new ProductPrice(BigDecimal.TEN),
                        objectMapper.createObjectNode(),
                        ProductUnit.PIECE
                ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(OutboxEvent.class)
                .returnResult()
                .getResponseBody();

        webTestClient.method(HttpMethod.DELETE)
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_DELETE)
                .bodyValue(new ProductDeleteCommand(new ProductId(outboxEvent.getAggregateId())))
                .exchange()
                .expectStatus()
                .isOk();

    }
}