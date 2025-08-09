package com.github.mangila.webshop.outbox.infrastructure.actuator;

import com.github.mangila.webshop.outbox.infrastructure.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.task.OutboxSimpleTaskRunner;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@WebEndpoint(id = "outboxtask")
@Component
public class OutboxTaskActuatorEndpoint {

    private final Map<String, OutboxTaskKey> outboxTaskKeys;
    private final OutboxSimpleTaskRunner outboxSimpleTaskRunner;

    public OutboxTaskActuatorEndpoint(Map<String, OutboxTaskKey> outboxTaskKeys,
                                      OutboxSimpleTaskRunner outboxSimpleTaskRunner) {
        this.outboxTaskKeys = outboxTaskKeys;
        this.outboxSimpleTaskRunner = outboxSimpleTaskRunner;
    }

    @ReadOperation
    public WebEndpointResponse<Map<String, Set<String>>> findAllTasks() {
        return new WebEndpointResponse<>(
                Map.of("tasks", outboxTaskKeys.keySet()),
                HttpStatus.OK.value()
        );
    }

    @WriteOperation
    public WebEndpointResponse<Map<String, Object>> execute(@Selector String key) {
        outboxSimpleTaskRunner.execute(new OutboxTaskKey(key));
        return new WebEndpointResponse<>(HttpStatus.NO_CONTENT.value());
    }
}
