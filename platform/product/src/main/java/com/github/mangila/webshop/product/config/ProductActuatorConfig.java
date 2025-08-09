package com.github.mangila.webshop.product.config;

import com.github.mangila.webshop.product.infrastructure.actuator.ProductTaskActuatorEndpoint;
import com.github.mangila.webshop.product.infrastructure.task.ProductTaskKey;
import com.github.mangila.webshop.product.infrastructure.task.ProductSimpleTaskRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ProductActuatorConfig {

    @Bean
    ProductTaskActuatorEndpoint productTaskActuatorEndpoint(
            Map<String, ProductTaskKey> nameToProductTaskKey,
            ProductSimpleTaskRunner productSimpleTaskRunner
    ) {
        return new ProductTaskActuatorEndpoint(nameToProductTaskKey, productSimpleTaskRunner);
    }

}
