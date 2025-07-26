package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private final OutboxCommandRepository commandRepository;
    private final SpringEventProducer springEventProducer;

    public MessageHandler(OutboxCommandRepository commandRepository, SpringEventProducer springEventProducer) {
        this.commandRepository = commandRepository;
        this.springEventProducer = springEventProducer;
    }

    @Transactional
    public void handle(OutboxId outboxId) {
        commandRepository.findByIdAndPublishedForUpdate(outboxId, OutboxPublished.notPublished())
                .ifPresentOrElse(
                        outboxMessage -> {
                            springEventProducer.produce(outboxMessage);
                            commandRepository.updatePublished(outboxMessage.id(), OutboxPublished.published());
                        }, () -> log.debug("Message locked or already processed with ID: {}", outboxId)
                );
    }
}
