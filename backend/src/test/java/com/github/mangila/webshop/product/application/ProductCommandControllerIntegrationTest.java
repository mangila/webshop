package com.github.mangila.webshop.product.application;

import com.github.mangila.webshop.TestcontainersConfiguration;
import com.github.mangila.webshop.product.ProductTestUtil;
import com.github.mangila.webshop.product.application.cqrs.ProductByIdQuery;
import com.github.mangila.webshop.product.application.dto.ProductDto;
import com.github.mangila.webshop.product.application.gateway.ProductServiceGateway;
import com.github.mangila.webshop.product.domain.ProductId;
import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.uuid.application.UuidGeneratorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it-test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class ProductCommandControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductServiceGateway productServiceGateway;

    @Autowired
    private UuidGeneratorService uuidGeneratorService;

    private final ProductTestUtil.TestProductInsertCommandBuilder insertCommandBuilder
            = new ProductTestUtil.TestProductInsertCommandBuilder();

    @Test
    @DisplayName("Insert one product and delete it")
    void insertAndDeleteProduct() {
        ProductDto dto = webTestClient.post()
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_INSERT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(insertCommandBuilder.buildDefault())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ProductDto.class)
                .returnResult()
                .getResponseBody();

        UUID productId = dto.id();

        boolean exists = productServiceGateway.query()
                .existsById(ProductId.from(productId));
        assertThat(exists).isTrue();

        boolean hasGenerated = uuidGeneratorService.hasGenerated(productId);
        assertThat(hasGenerated).isTrue();

        webTestClient.method(HttpMethod.DELETE)
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_DELETE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductByIdQuery(productId))
                .exchange()
                .expectStatus()
                .isOk();

        exists = productServiceGateway.query()
                .existsById(ProductId.from(productId));
        assertThat(exists).isFalse();
    }
}