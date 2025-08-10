package com.github.mangila.webshop.product.infrastructure.scheduler;

import com.github.mangila.webshop.product.infrastructure.scheduler.job.ProductJobKey;
import com.github.mangila.webshop.product.infrastructure.scheduler.job.ProductJobRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "app.product.scheduler.enabled", havingValue = "true")
public class ProductScheduler {

    private final ProductJobRunner taskRunner;

    public ProductScheduler(ProductJobRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    @Transactional
    @Scheduled(fixedRateString = "${app.product.scheduler.delete-marked.fixed-rate}")
    public void deleteMarkedProducts() {
        taskRunner.execute(new ProductJobKey("DELETE_MARKED_PRODUCTS"));
    }
}
