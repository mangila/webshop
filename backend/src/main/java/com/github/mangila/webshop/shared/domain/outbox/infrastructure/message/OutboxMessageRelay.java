package com.github.mangila.webshop.shared.domain.outbox.infrastructure.message;

import com.github.mangila.webshop.shared.application.message.rabbitmq.OutboxRabbitMessage;
import org.springframework.stereotype.Service;

@Service
public class OutboxMessageRelay {

    public void publish(OutboxRabbitMessage message) {

    }

}
