package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(MessageProcessor.class);
    private final OutboxCommandRepository commandRepository;
    private final SpringEventProducer springEventProducer;

    public MessageProcessor(OutboxCommandRepository commandRepository, SpringEventProducer springEventProducer) {
        this.commandRepository = commandRepository;
        this.springEventProducer = springEventProducer;
    }

    @Transactional
    public void processMessage(OutboxId outboxId) {
        commandRepository.findByIdAndPublishedForUpdate(outboxId, OutboxPublished.notPublished())
                .ifPresentOrElse(this::tryProcess, () -> log.debug("Message locked or processed already with ID: {}", outboxId));
    }

    private void tryProcess(OutboxMessage message) {
        log.debug("Relay Message with ID: {}", message.id());
        springEventProducer.produce(message);
        commandRepository.updatePublished(message.id(), OutboxPublished.published());
    }
}
