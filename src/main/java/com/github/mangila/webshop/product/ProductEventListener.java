package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.PgNotificationListener;
import com.github.mangila.webshop.product.model.ProductNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class ProductEventListener implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(ProductEventListener.class);

    private final ExecutorService executor;
    private final PgNotificationListener pgNotificationListener;
    private final ProductService service;

    public ProductEventListener(@Qualifier("virtualThreadExecutorProtoType") ExecutorService executor,
                                @Qualifier("productNotificationListener") PgNotificationListener pgNotificationListener,
                                ProductService service) {
        this.executor = executor;
        this.pgNotificationListener = pgNotificationListener;
        this.service = service;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        executor.submit(pgNotificationListener);
    }

    @EventListener
    public void on(PayloadApplicationEvent<ProductNotification> notification) {
        log.info("Received notification: {}", notification.getPayload());
    }

    @Override
    public void destroy() throws Exception {
        pgNotificationListener.shutdown();
        executor.shutdown();
        if (!executor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)) {
            log.warn("Executor did not terminate in the specified time. Will force shutdown");
            executor.shutdownNow();
        }
    }
}
