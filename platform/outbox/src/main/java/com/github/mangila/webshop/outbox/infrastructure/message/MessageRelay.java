package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.infrastructure.message.task.FillQueueOutboxTask;
import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskRunner;
import com.github.mangila.webshop.outbox.infrastructure.message.task.ProcessDlqOutboxTask;
import com.github.mangila.webshop.outbox.infrastructure.message.task.ProcessQueueOutboxTask;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.message-relay.enabled", havingValue = "true")
public class MessageRelay {
    private final OutboxTaskRunner outboxTaskRunner;

    public MessageRelay(OutboxTaskRunner outboxTaskRunner) {
        this.outboxTaskRunner = outboxTaskRunner;
    }

    @PostConstruct
    public void init() {
        outboxTaskRunner.execute(FillQueueOutboxTask.KEY);
    }

    @Scheduled(fixedRateString = "${app.message-relay.fill-queue-task.fixed-rate}")
    public void fillQueue() {
        outboxTaskRunner.execute(FillQueueOutboxTask.KEY);
    }

    @Scheduled(fixedRateString = "${app.message-relay.process-queue-task.fixed-rate}")
    public void processQueue() {
        outboxTaskRunner.execute(ProcessQueueOutboxTask.KEY);
    }

    @Scheduled(fixedRateString = "${app.message-relay.process-dlq-task.fixed-rate}")
    public void processDlq() {
        outboxTaskRunner.execute(ProcessDlqOutboxTask.KEY);
    }
}
