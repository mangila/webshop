package com.github.mangila.webshop.inventory.infrastructure.message;

import com.github.mangila.webshop.shared.infrastructure.config.RabbitMqConfig;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxMessageDto;
import com.github.mangila.webshop.shared.outbox.domain.message.OutboxMessage;
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
            containerFactory = "inventoryNewProductConsumerFactory")
    void listen(Message message) {
        OutboxMessageDto event = jsonMapper.toObject(message.getBodyAsBinary(), OutboxMessageDto.class);
        log.info("Consumed Message: {}", event);
    }
}
