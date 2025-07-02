package com.github.mangila.webshop.backend.event.domain.command;

public record EventSubscribeCommand(String consumer,
                                    String topic,
                                    String type) {
}
