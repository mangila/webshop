package com.github.mangila.webshop.app.actuator;

import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskRunner;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@WebEndpoint(id = "outboxtask")
@Component
public class OutboxTaskEndpoint {

    private final List<OutboxTaskKey> outboxTaskKeys;
    private final OutboxTaskRunner outboxTaskRunner;

    public OutboxTaskEndpoint(List<OutboxTaskKey> outboxTaskKeys,
                              OutboxTaskRunner outboxTaskRunner) {
        this.outboxTaskKeys = outboxTaskKeys;
        this.outboxTaskRunner = outboxTaskRunner;
    }

    @ReadOperation
    public WebEndpointResponse<Map<String, Object>> findAllTasks() {
        return new WebEndpointResponse<>(
                Map.of("outbox-task-keys", outboxTaskKeys),
                200
        );
    }

    @WriteOperation
    public WebEndpointResponse<Map<String, Object>> runTask(OutboxTaskKey key) {
        outboxTaskRunner.runTask(key);
        return new WebEndpointResponse<>(
                Map.of("key", key.value(), "status", "OK"),
                200
        );
    }
}
