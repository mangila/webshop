package com.github.mangila.webshop.outbox.infrastructure.scheduler;

import com.github.mangila.webshop.outbox.infrastructure.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.task.OutboxSimpleTaskRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.outbox.scheduler.enabled", havingValue = "true")
public class OutboxScheduler {

    private final OutboxSimpleTaskRunner outboxSimpleTaskRunner;

    public OutboxScheduler(OutboxSimpleTaskRunner outboxSimpleTaskRunner) {
        this.outboxSimpleTaskRunner = outboxSimpleTaskRunner;
    }

    @Scheduled(fixedRateString = "${app.outbox.scheduler.delete-published.fixed-rate}")
    public void deletePublished() {
        OutboxTaskKey key = outboxSimpleTaskRunner.findKey("DELETE_PUBLISHED");
        outboxSimpleTaskRunner.execute(key);
    }
}
