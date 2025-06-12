package com.github.mangila.webshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mangila.webshop.product.ProductEventService;
import com.github.mangila.webshop.product.model.event.CreateNewProductEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ApplicationTests {

    @Autowired
    private ProductEventService service;

    @Test
    void contextLoads() throws JsonProcessingException, InterruptedException {
        service.createProduct(
                new CreateNewProductEvent(
                        "abc123",
                        "hejsan",
                        "asdf",
                        new BigDecimal(123),
                        "",
                        "hej"
                )
        );
        Thread.sleep(10000);
    }

}
