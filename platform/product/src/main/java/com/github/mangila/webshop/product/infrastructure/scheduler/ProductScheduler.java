package com.github.mangila.webshop.product.infrastructure.scheduler;

import com.github.mangila.webshop.product.infrastructure.task.ProductSimpleTaskRunner;
import com.github.mangila.webshop.product.infrastructure.task.ProductTaskKey;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.product.scheduler.enabled", havingValue = "true")
public class ProductScheduler {

    private final ProductSimpleTaskRunner taskRunner;

    public ProductScheduler(ProductSimpleTaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    @Scheduled(fixedRateString = "10s")
    public void deleteProducts() {
        ProductTaskKey key = taskRunner.findKey("DELETE_PRODUCT");
        taskRunner.execute(key);
    }
}
