package com.github.mangila.webshop.product.infrastructure.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.product.scheduler.enabled", havingValue = "true")
public class ProductInboxScheduler {
}
