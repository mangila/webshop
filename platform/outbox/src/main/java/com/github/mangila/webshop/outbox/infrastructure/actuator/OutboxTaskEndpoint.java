package com.github.mangila.webshop.outbox.infrastructure.actuator;

import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskRunner;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;

import java.util.Map;

@WebEndpoint(id = "outboxtask")
@Component
public class OutboxTaskEndpoint {

    private final OutboxTaskRunner outboxTaskRunner;

    public OutboxTaskEndpoint(OutboxTaskRunner outboxTaskRunner) {
        this.outboxTaskRunner = outboxTaskRunner;
    }

    @ReadOperation
    public WebEndpointResponse<Map<String, Object>> readOperation(@Selector OutboxTaskKey key) {
        outboxTaskRunner.execute(key);
        return new WebEndpointResponse<>(
                Map.of("key", key.name(), "status", "OK"),
                200
        );
    }
}
