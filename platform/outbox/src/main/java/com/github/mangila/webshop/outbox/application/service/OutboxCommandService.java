package com.github.mangila.webshop.outbox.application.service;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxUpdated;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.Ensure;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OutboxCommandService {

    private final OutboxCommandRepository commandRepository;

    public OutboxCommandService(OutboxCommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Optional<OutboxMessage> findByIdForUpdate(OutboxId outboxId) {
        Ensure.activeSpringTransaction();
        return commandRepository.findByIdForUpdate(outboxId);
    }

    public OutboxSequence findByAggregateIdAndIncrementForUpdate(OutboxAggregateId aggregateId) {
        Ensure.activeSpringTransaction();
        return commandRepository.findAndLockByAggregateId(aggregateId)
                .map(OutboxSequence::incrementFrom)
                .orElseGet(() -> OutboxSequence.initial(aggregateId.value()));
    }

    public Outbox insert(OutboxInsertCommand command) {
        return commandRepository.insert(command);
    }

    public void updateSequence(OutboxSequence sequence) {
        commandRepository.updateSequence(sequence);
    }

    public void updateStatus(OutboxId id,
                             OutboxStatusType outboxStatusType,
                             OutboxUpdated outboxUpdated) {
        commandRepository.updateStatus(id, outboxStatusType, outboxUpdated);
    }
}
