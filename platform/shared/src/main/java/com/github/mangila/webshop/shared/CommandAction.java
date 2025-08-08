package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.model.OutboxEvent;

import java.util.function.Function;

public interface CommandAction<C extends Record> {

    Event event();

    OutboxEvent execute(C command);

    default Function<C, OutboxEvent> execute() {
        return this::execute;
    }
}
