package com.github.mangila.webshop.outbox.infrastructure.jpa;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageRelay;
import org.springframework.transaction.annotation.Transactional;

public class OutboxJpaCommandRepository implements OutboxCommandRepository {

    private final MessageRelay messageRelay;
    private final OutboxEntityCommandRepository jpaRepository;
    private final OutboxEntityMapper mapper;

    public OutboxJpaCommandRepository(MessageRelay messageRelay, OutboxEntityCommandRepository jpaRepository,
                                      OutboxEntityMapper mapper) {
        this.messageRelay = messageRelay;
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Outbox insert(OutboxInsertCommand command) {
        var entity = jpaRepository.save(mapper.toEntity(command));
        messageRelay.relay(mapper.toMessage(entity));
        return mapper.toDomain(entity);
    }

    @Transactional
    @Override
    public void updateAsPublished(OutboxId id) {

    }
}
