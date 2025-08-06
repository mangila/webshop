package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.outbox.scheduler.enabled", havingValue = "true")
public class OutboxScheduler {

    private final OutboxTaskRunner outboxTaskRunner;

    public OutboxScheduler(OutboxTaskRunner outboxTaskRunner) {
        this.outboxTaskRunner = outboxTaskRunner;
    }

    @Scheduled(fixedRateString = "${app.outbox.scheduler.delete-published.fixed-rate}")
    public void deletePublished() {
        OutboxTaskKey key = outboxTaskRunner.findKeyOrThrow("DELETE_PUBLISHED");
        outboxTaskRunner.runTask(key);
    }
}
