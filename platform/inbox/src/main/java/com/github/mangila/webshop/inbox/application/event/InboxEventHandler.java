package com.github.mangila.webshop.inbox.application.event;

import com.github.mangila.webshop.inbox.application.InboxCommandService;
import com.github.mangila.webshop.shared.model.InboxEvent;
import org.springframework.stereotype.Service;

@Service
public class InboxEventHandler {

    private final InboxCommandService commandService;
    private final InboxEventMapper eventMapper;

    public InboxEventHandler(InboxCommandService commandService,
                             InboxEventMapper eventMapper) {
        this.commandService = commandService;
        this.eventMapper = eventMapper;
    }

    public void handle(InboxEvent event) {
        var command = eventMapper.toCommand(event);
        commandService.insert(command);
    }
}
