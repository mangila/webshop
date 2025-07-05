package com.github.mangila.webshop.backend.product.application;

import com.github.mangila.webshop.backend.product.ProductTestUtil;
import com.github.mangila.webshop.backend.product.application.gateway.ProductServiceGateway;
import com.github.mangila.webshop.backend.product.application.web.ProductCommandController;
import com.github.mangila.webshop.backend.product.domain.command.ProductInsertCommand;
import com.github.mangila.webshop.backend.product.domain.model.ProductName;
import com.github.mangila.webshop.backend.product.domain.model.ProductPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

@WebMvcTest(ProductCommandController.class)
@MockitoBean(types = ProductServiceGateway.class)
class ProductCommandControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.webTestClient = MockMvcWebTestClient
                .bindTo(mockMvc)
                .build();
    }

    @ParameterizedTest(name = "Invalid product attributes: {0}")
    @MethodSource("invalidProductJsonTestCases")
    void testInvalidJson(InvalidProductJsonTestCase testCase) {
        webTestClient.post()
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_INSERT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testCase.json)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody();
    }

    record InvalidProductJsonTestCase(String description, String json) {
        @Override
        public String toString() {
            return description;
        }
    }

    static Stream<InvalidProductJsonTestCase> invalidProductJsonTestCases() {
        return Stream.of(
                createJsonTestCase("Attributes is JSON Array",
                        // language=JSON
                        """
                                   {"name":{"value":"Test Product"},"price":{"value":19.99},"attributes":[],"unit":"KILOGRAM"}
                                """
                ),
                createJsonTestCase("Attributes is JSON Value",
                        // language=JSON
                        """
                                   {"name":{"value":"Test Product"},"price":{"value":19.99},"attributes":"hej","unit":"KILOGRAM"}
                                """
                ),
                createJsonTestCase("Attributes is invalid JSON object",
                        // language=JSON
                        """
                                   {"name":{"value":"Test Product"},"price":{"value":19.99},"attributes":{"11","23"},"unit":"KILOGRAM"}
                                """
                ),
                createJsonTestCase("Empty body",
                        ""
                )
        );
    }

    private static InvalidProductJsonTestCase createJsonTestCase(String description, String json) {
        return new InvalidProductJsonTestCase(description, json);
    }

    @ParameterizedTest(name = "Invalid product validation: {0}")
    @MethodSource("invalidProductTestCases")
    void testInvalidProductInsertCommands(InvalidProductTestCase testCase) {
        webTestClient.post()
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_INSERT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testCase.command)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody();
    }

    record InvalidProductTestCase(String description, ProductInsertCommand command) {
        @Override
        public String toString() {
            return description;
        }
    }

    static Stream<InvalidProductTestCase> invalidProductTestCases() {
        return Stream.of(
                createTestCase("Null product name",
                        builder -> builder.withName(new ProductName(null))),
                createTestCase("Null product name",
                        builder -> builder.withName(null)),
                createTestCase("Empty product name",
                        builder -> builder.withName(new ProductName(""))),
                createTestCase("Null product price",
                        builder -> builder.withPrice(null)),
                createTestCase("Negative product price",
                        builder -> builder.withPrice(new ProductPrice(new BigDecimal("-1.00")))),
                createTestCase("Zero product price",
                        builder -> builder.withPrice(new ProductPrice(BigDecimal.ZERO))),
                createTestCase("Null attributes",
                        builder -> builder.withAttributes(null)),
                createTestCase("Null product unit",
                        builder -> builder.withUnit(null)),
                createTestCase("Extremely long product name",
                        builder -> builder.withName(new ProductName("a".repeat(256)))),
                createTestCase("Extremely high product price",
                        builder -> builder.withPrice(new ProductPrice(new BigDecimal("99999999999999.99"))))
        );
    }

    private static InvalidProductTestCase createTestCase(String description, Consumer<ProductTestUtil.TestProductInsertCommandBuilder> customizer) {
        ProductTestUtil.TestProductInsertCommandBuilder builder = new ProductTestUtil.TestProductInsertCommandBuilder();
        customizer.accept(builder);
        return new InvalidProductTestCase(description, builder.build());
    }
}