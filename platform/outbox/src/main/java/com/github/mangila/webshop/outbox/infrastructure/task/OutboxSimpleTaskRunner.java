package com.github.mangila.webshop.outbox.infrastructure.task;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SimpleTask;
import com.github.mangila.webshop.shared.SimpleTaskRunner;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OutboxSimpleTaskRunner implements SimpleTaskRunner<OutboxTaskKey> {
    private final Map<OutboxTaskKey, SimpleTask<OutboxTaskKey>> keyToOutboxTask;
    private final Map<String, OutboxTaskKey> nameToKey;

    public OutboxSimpleTaskRunner(Map<OutboxTaskKey, SimpleTask<OutboxTaskKey>> keyToOutboxTask,
                                  Map<String, OutboxTaskKey> nameToKey) {
        this.keyToOutboxTask = keyToOutboxTask;
        this.nameToKey = nameToKey;
    }

    @Override
    public OutboxTaskKey findKey(String taskKey) {
        Ensure.notNull(taskKey, String.class);
        OutboxTaskKey key = nameToKey.get(taskKey);
        Ensure.notNull(key, () -> new ResourceNotFoundException(
                OutboxTaskKey.class,
                taskKey
        ));
        return key;
    }

    @Override
    public void execute(OutboxTaskKey taskKey) {
        Ensure.notNull(taskKey, OutboxTaskKey.class);
        var task = keyToOutboxTask.get(taskKey);
        Ensure.notNull(task, () -> new ResourceNotFoundException(
                OutboxTaskKey.class,
                taskKey
        ));
        task.execute();
    }
}
