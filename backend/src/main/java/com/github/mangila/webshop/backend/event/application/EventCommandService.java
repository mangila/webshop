package com.github.mangila.webshop.backend.event.application;

import com.github.mangila.webshop.backend.event.domain.command.EventEmitCommand;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.infrastructure.EventCommandRepository;
import org.springframework.stereotype.Service;

@Service
public class EventCommandService {

    private final EventCommandRepository commandRepository;

    public EventCommandService(EventCommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Event emit(EventEmitCommand command) {
        return commandRepository.save(command.toNewEvent());
    }
}
