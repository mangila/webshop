package com.github.mangila.webshop.backend.product.config;

import com.github.mangila.webshop.backend.product.domain.ProductEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.DataClassRowMapper;

@Configuration
public class ProductConfig {

    @Bean
    public DataClassRowMapper<ProductEntity> productEntityRowMapper() {
        return new DataClassRowMapper<>(ProductEntity.class);
    }
}
