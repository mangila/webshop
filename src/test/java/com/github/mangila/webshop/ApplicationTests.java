package com.github.mangila.webshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mangila.webshop.product.ProductEventService;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEventType;
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
    void contextLoads() throws InterruptedException, JsonProcessingException {
        var product = new Product();
        product.setId("ID123");
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setCategory("Category");
        product.setPrice(new BigDecimal("12.00"));
        product.setImageUrl("https://www.google.com");
        product.setExtensions("""
                {"key": "value"}
                """);
        service.processMutation(ProductEventType.CREATE_NEW, product);
        Thread.sleep(10000);
    }

}
