package com.github.mangila.webshop.app.actuator;

import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskRunner;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@WebEndpoint(id = "outboxtask")
@Component
public class OutboxTaskEndpoint {

    private final Map<String, OutboxTaskKey> outboxTaskKeys;
    private final OutboxTaskRunner outboxTaskRunner;

    public OutboxTaskEndpoint(Map<String, OutboxTaskKey> outboxTaskKeys,
                              OutboxTaskRunner outboxTaskRunner) {
        this.outboxTaskKeys = outboxTaskKeys;
        this.outboxTaskRunner = outboxTaskRunner;
    }

    @ReadOperation
    public WebEndpointResponse<Map<String, Set<String>>> findAllTasks() {
        return new WebEndpointResponse<>(
                Map.of("tasks", outboxTaskKeys.keySet()),
                200
        );
    }

    @WriteOperation
    public WebEndpointResponse<Map<String, Object>> runTask(@Selector String key) {
        outboxTaskRunner.runTask(new OutboxTaskKey(key));
        return new WebEndpointResponse<>(
                Map.of("key", key, "status", "OK"),
                200
        );
    }
}
