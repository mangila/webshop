package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.AbstractEventListener;
import com.github.mangila.webshop.common.PgNotificationListener;
import com.github.mangila.webshop.product.model.ProductNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ProductEventListener implements AbstractEventListener<ProductNotification> {

    private static final Logger log = LoggerFactory.getLogger(ProductEventListener.class);

    private final ExecutorService executor;
    private final PgNotificationListener pgNotificationListener;
    private final ProductEventService service;

    public ProductEventListener(@Qualifier("virtualThreadExecutorProtoType") ExecutorService executor,
                                @Qualifier("productNotificationListener") PgNotificationListener pgNotificationListener,
                                ProductEventService service) {
        this.executor = executor;
        this.pgNotificationListener = pgNotificationListener;
        this.service = service;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        log.info("ProductEventListener is ready");
        executor.submit(pgNotificationListener);
    }

    @Override
    public void onEvent(ProductNotification event) {
        log.info("Received event: {}", event);
    }

    @Override
    public void handleException(Exception e, ProductNotification event) {

    }

    @Override
    public void shutdown() {
        pgNotificationListener.shutdown();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("Executor did not terminate in the specified time. Will force shutdown");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            log.error("Interrupted while waiting for executor to terminate", e);
        }
    }

    @Override
    public void destroy() {
        shutdown();
    }
}
