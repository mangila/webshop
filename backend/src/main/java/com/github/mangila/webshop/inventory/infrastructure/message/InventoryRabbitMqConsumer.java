package com.github.mangila.webshop.inventory.infrastructure.message;

import com.github.mangila.webshop.shared.infrastructure.config.RabbitMqConfig;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;
import com.github.mangila.webshop.shared.outbox.infrastructure.message.OutboxMessage;
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
            id = "inventoryNewProductConsumer",
            queues = RabbitMqConfig.PRODUCT_STREAM_KEY,
            containerFactory = "inventoryNewProductConsumer")
    void listen(Message message) {
        OutboxMessage event = jsonMapper.toObject(message.getBodyAsBinary(), OutboxMessage.class);
        log.info("Consumed Message: {}", event);
    }
}
