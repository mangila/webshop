package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.shared.Ensure;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OutboxTaskRunner {

    private final Map<OutboxTaskKey, OutboxTask> tasks;

    public OutboxTaskRunner(Map<String, OutboxTask> tasks) {
        this.tasks = tasks.values()
                .stream()
                .collect(Collectors.toMap(OutboxTask::key, Function.identity()));
    }
    public void execute(OutboxTaskKey key) {
        Ensure.notNull(key, "OutboxTaskKey must not be null");
        Ensure.isTrue(tasks.containsKey(key), "OutboxTask not exist: " + key);
        OutboxTask task = tasks.get(key);
        task.execute();
    }
}
