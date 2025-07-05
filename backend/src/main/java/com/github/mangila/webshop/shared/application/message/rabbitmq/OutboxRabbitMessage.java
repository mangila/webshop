package com.github.mangila.webshop.shared.application.message.rabbitmq;

public record OutboxRabbitMessage(long id, String topic, String type) {
}
