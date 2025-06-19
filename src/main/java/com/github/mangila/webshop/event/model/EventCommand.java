package com.github.mangila.webshop.event.model;

import com.github.mangila.webshop.common.util.annotation.AlphaNumeric;
import com.github.mangila.webshop.common.util.annotation.Json;
import jakarta.validation.constraints.NotNull;

public record EventCommand(
        @NotNull
        EventCommandType commandType,
        @NotNull
        @AlphaNumeric
        String topic,
        @NotNull
        String eventType,
        @NotNull
        @AlphaNumeric
        String aggregateId,
        @Json
        String data
) {
}
