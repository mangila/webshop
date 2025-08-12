package com.github.mangila.webshop.outbox.application.action.command;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.command.FindOutboxForUpdateCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxStatusCommand;
import com.github.mangila.webshop.shared.CommandAction;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class FindOutboxForUpdateCommandAction implements CommandAction<FindOutboxForUpdateCommand, Optional<Outbox>> {

    private static final Logger log = LoggerFactory.getLogger(FindOutboxForUpdateCommandAction.class);
    private final OutboxCommandRepository repository;

    public FindOutboxForUpdateCommandAction(OutboxCommandRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public Optional<Outbox> execute(@NotNull FindOutboxForUpdateCommand command) {
        return repository.findForUpdate(command)
                .filter(outbox -> {
                    if (outbox.canBePublished()) {
                        return true;
                    }
                    log.debug("Message: {} was already published", outbox.id());
                    return false;
                })
                .map(outbox -> {
                    if (outbox.processing()) {
                        log.debug("Message: {} has already processing status, early return", outbox.id());
                        return outbox;
                    }
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
