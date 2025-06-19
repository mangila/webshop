package com.github.mangila.webshop.event.command;

import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventCommand;
import org.springframework.stereotype.Service;

@Service
public class EventCommandGateway {

    private final EventCommandService commandService;

    public EventCommandGateway(EventCommandService commandService) {
        this.commandService = commandService;
    }

    public Event processCommand(EventCommand command) {
        var type = command.commandType();
        return switch (type) {
            case EMIT_EVENT -> commandService.emit(command);
            case null -> throw new IllegalArgumentException("Null command type");
            default -> throw new IllegalArgumentException("Unknown command type: " + command);
        };
    }
}
