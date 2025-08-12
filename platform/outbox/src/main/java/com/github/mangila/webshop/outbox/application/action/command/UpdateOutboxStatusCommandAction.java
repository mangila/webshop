package com.github.mangila.webshop.outbox.application.action.command;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxStatusCommand;
import com.github.mangila.webshop.shared.CommandAction;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UpdateOutboxStatusCommandAction implements CommandAction<UpdateOutboxStatusCommand, Boolean> {

    private static final Logger log = LoggerFactory.getLogger(UpdateOutboxStatusCommandAction.class);
    private final OutboxCommandRepository repository;

    public UpdateOutboxStatusCommandAction(OutboxCommandRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Boolean execute(@NotNull UpdateOutboxStatusCommand command) {
        log.debug("Updating status of outbox with id {}", command.id());
        repository.updateStatus(command);
        return Boolean.TRUE;
    }
}
