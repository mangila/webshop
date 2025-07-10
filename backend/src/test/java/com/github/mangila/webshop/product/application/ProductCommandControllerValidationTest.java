package com.github.mangila.webshop.product.application;

import com.github.mangila.webshop.product.ProductTestUtil;
import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.application.gateway.ProductServiceGateway;
import com.github.mangila.webshop.product.application.web.ProductCommandController;
import com.github.mangila.webshop.shared.application.dto.DomainMoneyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(ProductCommandController.class)
@MockitoBean(types = ProductServiceGateway.class)
class ProductCommandControllerValidationTest {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandControllerValidationTest.class);
    @Autowired
    private MockMvc mockMvc;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.webTestClient = MockMvcWebTestClient
                .bindTo(mockMvc)
                .build();
    }

    @ParameterizedTest(name = "validate product raw JSON: {0}")
    @MethodSource("notValidProductJsonTestCases")
    void testInvalidJson(NotValidProductJsonTestCase testCase) {
        ProblemDetail detail = webTestClient.post()
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_INSERT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testCase.json)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        log.info("Problem detail: {}", detail);
        assertThat(detail.getTitle())
                .isEqualTo("HTTP Message Not Readable");
    }

    record NotValidProductJsonTestCase(String description, String json) {
        @Override
        public String toString() {
            return description;
        }
    }

    static Stream<NotValidProductJsonTestCase> notValidProductJsonTestCases() {
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
                createJsonTestCase("Attributes is null",
                        // language=JSON
                        """
                                   {"name":{"value":"Test Product"},"price":{"value":19.99},"attributes":null,"unit":"KILOGRAM"}
                                """
                ),
                createJsonTestCase("Attributes is not a valid JSON object",
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

    private static NotValidProductJsonTestCase createJsonTestCase(String description, String json) {
        return new NotValidProductJsonTestCase(description, json);
    }

    @ParameterizedTest(name = "product validation: {0}")
    @MethodSource("notValidProductTestCases")
    void testInvalidProductInsertCommands(NotValidProductTestCase testCase) {
        ProblemDetail detail = webTestClient.post()
                .uri(ProductTestUtil.API_V1_PRODUCT_COMMAND_INSERT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testCase.command)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        log.info("Problem detail: {}", detail);
        assertThat(detail.getTitle())
                .isEqualTo("Validation Failed");
    }

    record NotValidProductTestCase(String description, ProductInsertCommand command) {
        @Override
        public String toString() {
            return description;
        }
    }

    static Stream<NotValidProductTestCase> notValidProductTestCases() {
        return Stream.of(
                // Product Name
                createTestCase("Null product name",
                        builder -> builder.withName(null)),
                createTestCase("Empty product name",
                        builder -> builder.withName("")),
                createTestCase("Invalid Alphanumeric product name",
                        builder -> builder.withName("ööö-product")),
                createTestCase("Extremely long product name",
                        builder -> builder.withName("a".repeat(256))),
                // Product Price
                createTestCase("Null product amount",
                        builder -> builder.withPrice(null)),
                createTestCase("Negative product amount",
                        builder -> builder.withPrice(new DomainMoneyDto(new BigDecimal("-1.00"), "USD"))),
                createTestCase("Zero product amount",
                        builder -> builder.withPrice(new DomainMoneyDto(BigDecimal.ZERO, "USD"))),
                createTestCase("Extremely high product amount",
                        builder -> builder.withPrice(new DomainMoneyDto(new BigDecimal("99999999999999.99"), "USD"))),
                // Product Attributes
                createTestCase("Null attributes",
                        builder -> builder.withAttributes(null)),
                // Product Unit
                createTestCase("Null product unit",
                        builder -> builder.withUnit(null)
                ));
    }

    private static NotValidProductTestCase createTestCase(String description, Consumer<ProductTestUtil.TestProductInsertCommandBuilder> customizer) {
        ProductTestUtil.TestProductInsertCommandBuilder builder = new ProductTestUtil.TestProductInsertCommandBuilder();
        customizer.accept(builder);
        return new NotValidProductTestCase(description, builder.build());
    }
}