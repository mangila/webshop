package com.github.mangila.webshop.outbox.application.action.command;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.command.CreateOutboxCommand;
import com.github.mangila.webshop.shared.CommandAction;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CreateOutboxCommandAction implements CommandAction<CreateOutboxCommand, Outbox> {

    private final OutboxCommandRepository repository;

    public CreateOutboxCommandAction(OutboxCommandRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Outbox execute(@NotNull CreateOutboxCommand command) {
        return repository.insert(command);
    }
}
