package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxDomainMessageQueue;
import com.github.mangila.webshop.shared.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FillQueuesOutboxTask implements OutboxTask {
    private static final Logger log = LoggerFactory.getLogger(FillQueuesOutboxTask.class);
    private final OutboxQueryRepository queryRepository;
    private final Map<Domain, OutboxDomainMessageQueue> domainQueues;

    public FillQueuesOutboxTask(OutboxQueryRepository queryRepository,
                                Map<Domain, OutboxDomainMessageQueue> domainQueues) {
        this.queryRepository = queryRepository;
        this.domainQueues = domainQueues;
    }

    @Override
    public void execute() {
        queryRepository.findAllByStatus(OutboxStatusType.PENDING, 120)
                .stream()
                .peek(message -> log.info("Queue Message: {} - {}", message.id(), message.domain()))
                .forEach(message -> {
                    OutboxDomainMessageQueue queue = domainQueues.get(message.domain());
                    queue.add(message.id());
                });
    }

    @Override
    public OutboxTaskKey key() {
        return new OutboxTaskKey("FILL_QUEUES");
    }
}
