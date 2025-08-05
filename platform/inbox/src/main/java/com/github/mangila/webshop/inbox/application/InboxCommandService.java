package com.github.mangila.webshop.inbox.application;

import com.github.mangila.webshop.inbox.domain.Inbox;
import com.github.mangila.webshop.inbox.domain.cqrs.InboxInsertCommand;
import org.springframework.stereotype.Service;

@Service
public class InboxCommandService {
    public Inbox insert(InboxInsertCommand command) {
        return new Inbox();
    }
}
