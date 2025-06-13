package com.github.mangila.webshop;

import com.github.mangila.webshop.product.ProductCommandService;
import com.github.mangila.webshop.product.ProductEventService;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Map;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ApplicationTests {

    @Autowired
    private ProductEventService service;

    @Test
    void contextLoads() throws InterruptedException {
        var product = new Product();
        product.setId("ID123");
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setCategory("Category");
        product.setPrice(new BigDecimal("12.00"));
        product.setImageUrl("https://www.google.com");
        product.setExtensions(Map.of("key", "valie"));
        service.processMutation(ProductEventType.CREATE_NEW, product);
        Thread.sleep(10000);
    }

}
