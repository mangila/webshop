package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.model.Event;
import io.vavr.control.Try;
import jakarta.validation.constraints.NotNull;

import java.util.function.Function;

public interface CommandAction<C, R> {

    R execute(@NotNull C command);

    default Function<C, R> execute() {
        return this::execute;
    }

    default Try<R> tryExecute(C command) {
        return Try.of(() -> execute(command));
    }

    default Event event() {
        return Event.EMPTY;
    }
}
