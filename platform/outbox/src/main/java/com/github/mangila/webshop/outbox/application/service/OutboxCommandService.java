package com.github.mangila.webshop.outbox.application.service;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxUpdateStatusCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.projection.OutboxProjection;
import com.github.mangila.webshop.shared.Ensure;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class OutboxCommandService {

    private final OutboxCommandRepository commandRepository;

    public OutboxCommandService(OutboxCommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Optional<OutboxProjection> findByIdForUpdate(@NotNull OutboxId outboxId) {
        Ensure.activeSpringTransaction();
        return commandRepository.findByIdForUpdate(outboxId);
    }

    public OutboxSequence findByAggregateIdAndIncrementForUpdate(@NotNull OutboxAggregateId aggregateId) {
        Ensure.activeSpringTransaction();
        return commandRepository.findAndLockByAggregateId(aggregateId)
                .map(OutboxSequence::incrementFrom)
                .orElseGet(() -> OutboxSequence.initial(aggregateId.value()));
    }

    public Outbox insert(@NotNull OutboxInsertCommand command) {
        return commandRepository.insert(command);
    }

    public void updateSequence(@NotNull OutboxSequence sequence) {
        commandRepository.updateSequence(sequence);
    }

    public void updateStatus(@NotNull OutboxUpdateStatusCommand command) {
        commandRepository.updateStatus(command);
    }

    public void deleteByIds(List<OutboxId> ids) {
        commandRepository.deleteByIds(ids);
    }
}
