package com.github.mangila.webshop.outbox.application.action.command;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.command.IncrementOutboxSequenceCommand;
import com.github.mangila.webshop.shared.CommandAction;
import com.github.mangila.webshop.shared.Ensure;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class IncrementOutboxSequenceCommandAction implements CommandAction<IncrementOutboxSequenceCommand, OutboxSequence> {

    private final OutboxCommandRepository repository;

    public IncrementOutboxSequenceCommandAction(OutboxCommandRepository repository) {
        this.repository = repository;
    }

    @Override
    public OutboxSequence execute(@NotNull IncrementOutboxSequenceCommand command) {
        Ensure.activeSpringTransaction();
        return repository.findCurrentSequence(command.id())
                .map(OutboxSequence::incrementFrom)
                .orElseGet(() -> OutboxSequence.initial(command.id()));
    }
}
