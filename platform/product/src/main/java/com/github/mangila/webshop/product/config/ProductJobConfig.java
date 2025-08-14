package com.github.mangila.webshop.product.config;

import com.github.mangila.webshop.product.application.action.command.DeleteProductCommandAction;
import com.github.mangila.webshop.product.application.action.query.FindProductsByStatusQueryAction;
import com.github.mangila.webshop.product.infrastructure.scheduler.job.DeleteMarkedProductsJob;
import com.github.mangila.webshop.product.infrastructure.scheduler.job.ProductJobKey;
import com.github.mangila.webshop.shared.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ProductJobConfig {

    private static final Logger log = LoggerFactory.getLogger(ProductJobConfig.class);

    private final FindProductsByStatusQueryAction findProductsByStatusQueryAction;
    private final DeleteProductCommandAction deleteProductCommandAction;

    public ProductJobConfig(FindProductsByStatusQueryAction findProductsByStatusQueryAction,
                            DeleteProductCommandAction deleteProductCommandAction) {
        this.findProductsByStatusQueryAction = findProductsByStatusQueryAction;
        this.deleteProductCommandAction = deleteProductCommandAction;
    }

    @Bean
    Map<ProductJobKey, SimpleJob<ProductJobKey>> keyToProductJob() {
        return Map.ofEntries(
                addJob(new DeleteMarkedProductsJob(findProductsByStatusQueryAction, deleteProductCommandAction))
        );
    }

    private Map.Entry<ProductJobKey, SimpleJob<ProductJobKey>> addJob(SimpleJob<ProductJobKey> job) {
        log.info("Add Job: {}", job.key());
        return Map.entry(job.key(), job);
    }

}
