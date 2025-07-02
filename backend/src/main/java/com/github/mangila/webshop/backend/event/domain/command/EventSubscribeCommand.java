package com.github.mangila.webshop.backend.event.domain.command;

import com.github.mangila.webshop.backend.event.domain.common.EventSubscriberProperties;

public record EventSubscribeCommand(String consumer,
                                    String topic,
                                    String type) {

    public static EventSubscribeCommand from(EventSubscriberProperties properties) {
        return new EventSubscribeCommand(properties.consumer(), properties.topic(), properties.type());
    }

}
