package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.outbox.infrastructure.message.InternalMessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FillQueueOutboxTask implements OutboxTask {

    private static final Logger log = LoggerFactory.getLogger(FillQueueOutboxTask.class);
    private final OutboxQueryRepository queryRepository;
    private final InternalMessageQueue internalMessageQueue;

    public FillQueueOutboxTask(OutboxQueryRepository queryRepository,
                               InternalMessageQueue internalMessageQueue) {
        this.queryRepository = queryRepository;
        this.internalMessageQueue = internalMessageQueue;
    }

    @Override
    public void execute() {
        queryRepository.findAllIdsByStatus(OutboxStatusType.PENDING, 10)
                .stream()
                .peek(id -> log.info("Queue Message: {}", id))
                .forEach(internalMessageQueue::add);
    }

    @Override
    public OutboxTaskKey key() {
        return OutboxTaskKey.FILL_QUEUE;
    }
}
