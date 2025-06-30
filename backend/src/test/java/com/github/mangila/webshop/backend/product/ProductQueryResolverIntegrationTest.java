package com.github.mangila.webshop.backend.product;

import com.github.mangila.webshop.backend.TestcontainersConfiguration;
import com.github.mangila.webshop.backend.product.domain.ProductDomain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureGraphQlTester
@Import(TestcontainersConfiguration.class)
class ProductQueryResolverIntegrationTest {

    @Autowired
    private GraphQlTester graphQlTester;
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
    @DisplayName("Should return product when finding by valid ID")
    void shouldReturnProductWhenFindingByValidId() {
        var id = ProductTestUtil.dummyProductId();
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
                """.formatted(id);

        graphQlTester.document(query)
                .execute()
                .path("findProductById")
                .entity(ProductDomain.class)
                .satisfies(product -> {
                    assertThat(product.id()).isEqualTo(id);
                    assertThat(product.name()).isEqualTo(ProductTestUtil.dummyProductName());
                    assertThat(product.price()).isEqualTo(ProductTestUtil.dummyProductPrice());
                    assertThat(product.attributes()).isNotNull();
                    assertThat(product.attributes().toString()).isEqualTo(ProductTestUtil.dummyProductAttributes());
                });
    }

    @Test
    @DisplayName("Should return NOT_FOUND error when finding product with non-existent ID")
    void shouldReturnNotFoundErrorWhenFindingProductWithNonExistentId() {
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
                .errors()
                .expect(err -> err.getErrorType().equals(ErrorType.NOT_FOUND))
                .expect(err -> err.getMessage().equals("id not found: 'non-existent-id'"))
                .verify();
    }
}
