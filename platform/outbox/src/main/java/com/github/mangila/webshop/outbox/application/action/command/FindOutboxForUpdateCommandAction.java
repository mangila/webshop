package com.github.mangila.webshop.outbox.application.action.command;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.command.FindOutboxForUpdateCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxStatusCommand;
import com.github.mangila.webshop.shared.CommandAction;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class FindOutboxForUpdateCommandAction implements CommandAction<FindOutboxForUpdateCommand, Optional<Outbox>> {

    private final OutboxCommandRepository repository;

    public FindOutboxForUpdateCommandAction(OutboxCommandRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public Optional<Outbox> execute(@NotNull FindOutboxForUpdateCommand command) {
        return repository.findForUpdate(command)
                .filter(Outbox::notPublished)
                .map(outbox -> {
                    var processing = UpdateOutboxStatusCommand.processing(outbox.id());
                    repository.updateStatus(processing);
                    return updated(outbox, processing);
                });
    }

    private static Outbox updated(Outbox outbox, UpdateOutboxStatusCommand command) {
        return new Outbox(
                outbox.id(),
                outbox.domain(),
                outbox.event(),
                outbox.aggregateId(),
                outbox.payload(),
                command.outboxStatusType(),
                outbox.sequence(),
                command.outboxUpdated(),
                outbox.created()
        );
    }
}
