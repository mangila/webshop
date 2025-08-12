package com.github.mangila.webshop.outbox.application.action.command;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxSequenceCommand;
import com.github.mangila.webshop.shared.CommandAction;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UpdateOutboxSequenceCommandAction implements CommandAction<UpdateOutboxSequenceCommand, Void> {
    private final OutboxCommandRepository repository;

    public UpdateOutboxSequenceCommandAction(OutboxCommandRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Void execute(@NotNull UpdateOutboxSequenceCommand command) {
        repository.updateSequence(command);
        return null;
    }
}
