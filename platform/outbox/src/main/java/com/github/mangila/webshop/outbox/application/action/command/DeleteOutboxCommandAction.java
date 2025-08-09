package com.github.mangila.webshop.outbox.application.action.command;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.command.DeleteOutboxCommand;
import com.github.mangila.webshop.shared.CommandAction;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class DeleteOutboxCommandAction implements CommandAction<DeleteOutboxCommand, Void> {
    private final OutboxCommandRepository repository;

    public DeleteOutboxCommandAction(OutboxCommandRepository repository) {
        this.repository = repository;
    }

    @Override
    public Void execute(DeleteOutboxCommand command) {
        repository.delete(command);
        return null;
    }
}
