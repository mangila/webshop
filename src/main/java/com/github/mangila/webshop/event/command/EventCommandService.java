package com.github.mangila.webshop.event.command;

import com.github.mangila.webshop.event.model.*;
import org.springframework.stereotype.Service;

@Service
public class EventCommandService {

    private final EventMapper eventMapper;
    private final EventCommandRepository commandRepository;

    public EventCommandService(EventMapper eventMapper,
                               EventCommandRepository commandRepository) {
        this.eventMapper = eventMapper;
        this.commandRepository = commandRepository;
    }

    public Event emit(EventCommand command) {
        EventEntity entity = eventMapper.toEntity(command);
        return commandRepository.emit(entity)
                .orElseThrow(() -> new EventEmitException(command.toString()));
    }
}
