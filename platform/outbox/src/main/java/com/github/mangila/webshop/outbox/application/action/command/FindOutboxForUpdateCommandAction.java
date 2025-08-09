package com.github.mangila.webshop.outbox.application.action.command;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.command.FindOutboxForUpdateCommand;
import com.github.mangila.webshop.shared.CommandAction;
import com.github.mangila.webshop.shared.Ensure;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class FindOutboxForUpdateCommandAction implements CommandAction<FindOutboxForUpdateCommand, Optional<Outbox>> {

    private final OutboxCommandRepository repository;

    public FindOutboxForUpdateCommandAction(OutboxCommandRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Outbox> execute(@NotNull FindOutboxForUpdateCommand command) {
        Ensure.activeSpringTransaction();
        return repository.findForUpdate(command);
    }
}
