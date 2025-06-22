package com.github.mangila.webshop.backend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.backend.TestcontainersConfiguration;
import com.github.mangila.webshop.backend.product.command.model.ProductUpsertCommand;
import com.github.mangila.webshop.backend.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@SpringBootTest(classes = com.github.mangila.webshop.backend.Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureGraphQlTester
@Import(TestcontainersConfiguration.class)
class ProductQueryResolverIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String PRODUCT_ID = "testgraphqlproduct";
    private static final String PRODUCT_NAME = "Test GraphQL Product";
    private static final BigDecimal PRODUCT_PRICE = new BigDecimal("59.99");
    private static final String PRODUCT_ATTRIBUTES = "{\"color\":\"purple\",\"size\":\"medium\"}";

    @BeforeEach
    void setUp() {
        // Create a test product to query
        ProductUpsertCommand command = new ProductUpsertCommand(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_PRICE,
                PRODUCT_ATTRIBUTES
        );

        webTestClient.post().uri("/api/v1/product/command/upsert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void findProductById_shouldReturnProduct() {
        // language=GraphQL
        String query = """
                query {
                  findProductById(input: {id: "%s"}) {
                    id
                    name
                    price
                    attributes
                  }
                }
                """.formatted(PRODUCT_ID);

        graphQlTester.document(query)
                .execute()
                .path("findProductById")
                .entity(Product.class)
                .satisfies(product -> {
                    assert product.id().equals(PRODUCT_ID);
                    assert product.name().equals(PRODUCT_NAME);
                    assert product.price().equals(PRODUCT_PRICE);
                    assert product.attributes().get("color").asText().equals("purple");
                    assert product.attributes().get("size").asText().equals("medium");
                });
    }

    @Test
    void findProductById_withNonExistentId_shouldReturnNull() {
        // language=GraphQL
        String query = """
                query {
                  findProductById(input: {id: "non-existent-id"}) {
                    id
                    name
                    price
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("findProductById")
                .valueIsNull();
    }
}
