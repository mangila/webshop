package com.github.mangila.webshop.product.config;

import com.github.mangila.webshop.product.infrastructure.actuator.ProductJobActuatorEndpoint;
import com.github.mangila.webshop.product.infrastructure.scheduler.job.ProductJobKey;
import com.github.mangila.webshop.product.infrastructure.scheduler.job.ProductJobRunner;
import com.github.mangila.webshop.shared.SimpleJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ProductActuatorConfig {

    @Bean
    ProductJobActuatorEndpoint productJobActuatorEndpoint(
            Map<ProductJobKey, SimpleJob<ProductJobKey>> keyToProductJob,
            ProductJobRunner productJobRunner
    ) {
        return new ProductJobActuatorEndpoint(keyToProductJob, productJobRunner);
    }

}
