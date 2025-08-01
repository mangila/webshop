package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
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
        Ensure.notNull(value, String.class);
        OutboxTaskKey key = outboxTaskKeys.get(value);
        Ensure.notNull(key, () -> new ResourceNotFoundException(
                OutboxTaskKey.class,
                value
        ));
        return key;
    }

    public void runTask(OutboxTaskKey key) {
        Ensure.notNull(key, OutboxTaskKey.class);
        OutboxTask task = tasks.get(key);
        Ensure.notNull(task, () -> new ResourceNotFoundException(
                OutboxTaskKey.class,
                key
        ));
        task.execute();
    }
}
