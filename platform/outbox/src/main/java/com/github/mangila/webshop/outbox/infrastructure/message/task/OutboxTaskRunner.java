package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OutboxTaskRunner {

    private final Map<OutboxTaskKey, OutboxTask> tasks;
    private final List<OutboxTaskKey> outboxTaskKeys;

    public OutboxTaskRunner(Map<OutboxTaskKey, OutboxTask> outboxTasks,
                            List<OutboxTaskKey> outboxTaskKeys) {
        this.tasks = outboxTasks;
        this.outboxTaskKeys = outboxTaskKeys;
    }

    public OutboxTaskKey findKeyOrThrow(String value) {
        return outboxTaskKeys.stream()
                .filter(key -> key.value().equals(value))
                .findFirst()
                .orElseThrow(() -> new ApplicationException("OutboxTaskKey not found: " + value));
    }

    public void runTask(OutboxTaskKey key) {
        Ensure.notNull(key, "OutboxTaskKey must not be null");
        Ensure.isTrue(tasks.containsKey(key), "OutboxTask not exist: " + key);
        OutboxTask task = tasks.get(key);
        task.execute();
    }
}
