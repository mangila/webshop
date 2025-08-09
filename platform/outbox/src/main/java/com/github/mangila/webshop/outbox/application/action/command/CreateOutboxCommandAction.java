package com.github.mangila.webshop.outbox.application.action.command;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.command.CreateOutboxCommand;
import com.github.mangila.webshop.shared.CommandAction;
import com.github.mangila.webshop.shared.Ensure;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CreateOutboxCommandAction implements CommandAction<CreateOutboxCommand, Outbox> {

    private final OutboxCommandRepository repository;

    public CreateOutboxCommandAction(OutboxCommandRepository repository) {
        this.repository = repository;
    }

    @Override
    public Outbox execute(@NotNull CreateOutboxCommand command) {
        Ensure.activeSpringTransaction();
        return repository.insert(command);
    }
}
