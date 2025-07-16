package com.github.mangila.webshop.product.application.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mangila.webshop.product.ProductTestUtil;
import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.application.gateway.ProductServiceGateway;
import com.github.mangila.webshop.shared.application.dto.DomainMoneyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @ParameterizedTest(name = "raw JSON: {0}")
    @MethodSource("notValidProductJsonTestCases")
    @DisplayName("Validate raw JSON")
    void shouldValidateJson(NotValidProductJsonTestCase testCase) {
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
                                 {
                                  "name" : "Test-Product",
                                  "price" : {
                                    "currency" : "USD",
                                    "amount" : "19.99"
                                  },
                                  "attributes" : [1,2,3],
                                  "unit" : "PIECE"
                                }
                                """
                ),
                createJsonTestCase("Attributes is JSON Key/Value",
                        // language=JSON
                        """
                                 {
                                  "name" : "Test-Product",
                                  "price" : {
                                    "currency" : "USD",
                                    "amount" : "19.99"
                                  },
                                  "attributes" : "json value",
                                  "unit" : "PIECE"
                                }
                                """
                ),
                createJsonTestCase("Attributes is not a valid JSON object",
                        // language=JSON
                        """
                                 {
                                  "name" : "Test-Product",
                                  "price" : {
                                    "currency" : "USD",
                                    "amount" : "19.99"
                                  },
                                  "attributes" : {"a","ab"},
                                  "unit" : "PIECE"
                                }
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

    @ParameterizedTest(name = "command: {0}")
    @MethodSource("notValidProductInsertCommandTestCases")
    @DisplayName("Validate ProductInsertCommand")
    void shouldValidateProductInsertCommands(NotValidProductInsertCommandTestCase testCase) throws JsonProcessingException {
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

    record NotValidProductInsertCommandTestCase(String description, ProductInsertCommand command) {
        @Override
        public String toString() {
            return description;
        }
    }

    static Stream<NotValidProductInsertCommandTestCase> notValidProductInsertCommandTestCases() {
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
                        builder -> builder.withPrice(new DomainMoneyDto("USD", new BigDecimal("-1.00")))),
                createTestCase("Zero product amount",
                        builder -> builder.withPrice(new DomainMoneyDto("USD", BigDecimal.ZERO))),
                createTestCase("Extremely high product amount",
                        builder -> builder.withPrice(new DomainMoneyDto("USD", new BigDecimal("99999999999999.99")))),
                // Product Attributes
                createTestCase("Null attributes",
                        builder -> builder.withAttributes(null)),
                // Product Unit
                createTestCase("Null product unit",
                        builder -> builder.withUnit(null)
                ));
    }

    private static NotValidProductInsertCommandTestCase createTestCase(String description, Consumer<ProductTestUtil.TestProductInsertCommandBuilder> customizer) {
        ProductTestUtil.TestProductInsertCommandBuilder builder = new ProductTestUtil.TestProductInsertCommandBuilder();
        customizer.accept(builder);
        return new NotValidProductInsertCommandTestCase(description, builder.build());
    }
}