package com.github.mangila.webshop.outbox.infrastructure.actuator;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.OutboxIdDistinctQueue;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxPublisher;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@WebEndpoint(id = "outboxpublish")
@Component
public class OutboxPublishActuatorEndpoint {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublishActuatorEndpoint.class);
    private final OutboxPublisher outboxPublisher;
    private final OutboxIdDistinctQueue outboxIdDistinctQueue;

    public OutboxPublishActuatorEndpoint(OutboxPublisher outboxPublisher,
                                         OutboxIdDistinctQueue outboxIdDistinctQueue) {
        this.outboxPublisher = outboxPublisher;
        this.outboxIdDistinctQueue = outboxIdDistinctQueue;
    }

    @WriteOperation
    public WebEndpointResponse<Map<String, Object>> execute(@Selector long outboxId) {
        boolean ok = outboxPublisher.tryPublish(new OutboxId(outboxId))
                .getOrElseThrow(throwable -> new ApplicationException("Failed to publish outbox: %d".formatted(outboxId), throwable));
        if (!ok) {
            log.error("Failed to publish outbox: {}", outboxId);
        }
        return new WebEndpointResponse<>(HttpStatus.NO_CONTENT.value());
    }

    @WriteOperation
    public WebEndpointResponse<Map<String, Object>> execute(@Selector List<Long> outboxIds) {
        var ids = outboxIds.stream()
                .map(OutboxId::new)
                .toList();
        outboxIdDistinctQueue.fillDlq(ids);
        log.debug("Fill Dlq with {} outbox IDs for processing -- {}", ids.size(), ids);
        return new WebEndpointResponse<>(HttpStatus.NO_CONTENT.value());
    }
}
