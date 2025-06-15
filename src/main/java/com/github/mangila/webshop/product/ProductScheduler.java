package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.event.Event;
import com.github.mangila.webshop.common.event.EventTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductScheduler {

    private static final Logger log = LoggerFactory.getLogger(ProductScheduler.class);

    private final EventTopic topic = EventTopic.PRODUCT;
    private final ProductEventService productEventService;

    public ProductScheduler(ProductEventService productEventService) {
        this.productEventService = productEventService;
    }

    @Scheduled(initialDelay = 10, fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void acknowledgeEvents() {
        List<Event> events = productEventService.queryPendingEventsByTopic(topic);
        log.info("Found {} events to acknowledge", events.size());
    }

}
