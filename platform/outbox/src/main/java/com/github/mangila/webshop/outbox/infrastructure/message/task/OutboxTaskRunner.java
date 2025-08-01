package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.shared.Ensure;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OutboxTaskRunner {

    private final Map<OutboxTaskKey, OutboxTask> tasks;
    private final Map<String, OutboxTaskKey> outboxTaskKeys;

    public OutboxTaskRunner(Map<OutboxTaskKey, OutboxTask> outboxTasks,
                            Map<String, OutboxTaskKey> outboxTaskKeys) {
        this.tasks = outboxTasks;
        this.outboxTaskKeys = outboxTaskKeys;
    }

    public OutboxTaskKey findKeyOrThrow(String value) {
        Ensure.notNull(value, "OutboxTaskKey value must not be null");
        OutboxTaskKey key = outboxTaskKeys.get(value);
        Ensure.notNull(key, "OutboxTaskKey not found: %s".formatted(value));
        return key;
    }

    public void runTask(OutboxTaskKey key) {
        Ensure.notNull(key, "OutboxTaskKey must not be null");
        OutboxTask task = tasks.get(key);
        Ensure.notNull(task, "OutboxTask not found: %s".formatted(key));
        task.execute();
    }
}
