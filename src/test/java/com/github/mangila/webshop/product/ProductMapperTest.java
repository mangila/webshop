package com.github.mangila.webshop.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.product.model.ProductMutate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        ProductMapper.class,
        JsonMapper.class,
        ObjectMapper.class})
class ProductMapperTest {

    @Autowired
    private ProductMapper mapper;

    @Test
    void test() {
        var mutate = new ProductMutate(null,
                null,
                null,
                null,
                null,
                null,
                null);
        var product = mapper.toProduct(mutate);
        assertThat(product).isNotNull();
    }
}