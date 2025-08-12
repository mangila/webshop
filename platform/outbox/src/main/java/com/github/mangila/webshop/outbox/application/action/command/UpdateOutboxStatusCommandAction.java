package com.github.mangila.webshop.outbox.application.action.command;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxStatusCommand;
import com.github.mangila.webshop.shared.CommandAction;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UpdateOutboxStatusCommandAction implements CommandAction<UpdateOutboxStatusCommand, Void> {

    private final OutboxCommandRepository repository;

    public UpdateOutboxStatusCommandAction(OutboxCommandRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Void execute(@NotNull UpdateOutboxStatusCommand command) {
        repository.updateStatus(command);
        return null;
    }
}
