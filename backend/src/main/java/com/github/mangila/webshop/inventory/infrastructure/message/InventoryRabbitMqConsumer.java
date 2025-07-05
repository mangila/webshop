package com.github.mangila.webshop.inventory.infrastructure.message;

import com.github.mangila.webshop.shared.application.json.JsonMapper;
import com.github.mangila.webshop.shared.infrastructure.RabbitMqConfig;
import com.github.mangila.webshop.outboxevent.domain.OutboxEvent;
import com.rabbitmq.stream.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryRabbitMqConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryRabbitMqConsumer.class);

    private final JsonMapper jsonMapper;

    public InventoryRabbitMqConsumer(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(
            queues = RabbitMqConfig.OUTBOX_EVENT_PRODUCT_STREAM_KEY,
            containerFactory = "inventoryNewProductConsumer")
    void listen(Message message) {
        OutboxEvent event = jsonMapper.toObject(message.getBodyAsBinary(), OutboxEvent.class);
        log.info("Consumed Message: {}", event);
    }
}
