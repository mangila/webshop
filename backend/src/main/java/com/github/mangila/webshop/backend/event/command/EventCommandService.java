package com.github.mangila.webshop.backend.event.command;

import com.github.mangila.webshop.backend.common.util.exception.CommandException;
import com.github.mangila.webshop.backend.event.command.model.EventEmitCommand;
import com.github.mangila.webshop.backend.event.model.Event;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EventCommandService {

    private final EventCommandRepository commandRepository;

    public EventCommandService(EventCommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Event emit(EventEmitCommand command) {
        return commandRepository.emit(command)
                .orElseThrow(() -> new CommandException(
                        command.getClass(),
                        Event.class,
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Emit Event failed"
                ));
    }
}
