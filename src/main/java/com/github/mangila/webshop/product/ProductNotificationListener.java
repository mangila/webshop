package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.AbstractNotificationListener;
import com.github.mangila.webshop.common.EventService;
import com.github.mangila.webshop.common.PgNotificationListener;
import com.github.mangila.webshop.product.model.ProductNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ProductNotificationListener implements AbstractNotificationListener<ProductNotification> {

    private static final Logger log = LoggerFactory.getLogger(ProductNotificationListener.class);

    private final ExecutorService executor;
    private final PgNotificationListener pgNotificationListener;
    private final EventService eventService;
    private final ProductCommandService service;

    public ProductNotificationListener(@Qualifier("virtualThreadExecutor") ExecutorService executor,
                                       @Qualifier("productPgNotificationListener") PgNotificationListener pgNotificationListener, EventService eventService,
                                       ProductCommandService service) {
        this.executor = executor;
        this.pgNotificationListener = pgNotificationListener;
        this.eventService = eventService;
        this.service = service;
    }

    @Override
    public void onReady() {
        executor.submit(pgNotificationListener);
    }

    @Override
    public void onNotification(ProductNotification notification) {
        try {
            log.info("Received notification: {} -- {}", notification.getId(), notification.getTopic());
            var event = eventService.acknowledgeEvent(notification.getId(), notification.getTopic());
            log.info("New event received: {}", event);
        } catch (Exception e) {
            handleException(e, notification);
        }
    }

    @Override
    public void handleException(Exception e, ProductNotification notification) {
        log.error("Failed to process notification: {}", notification, e);
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
