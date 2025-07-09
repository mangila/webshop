package com.github.mangila.webshop.shared.outbox.infrastructure.rabbitmq;

import com.github.mangila.webshop.inventory.application.config.InventoryDomainRegistryConfig;
import com.github.mangila.webshop.product.application.config.ProductRegistryConfig;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;
import com.github.mangila.webshop.shared.outbox.infrastructure.message.OutboxMessage;
import com.rabbitmq.stream.Message;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.github.mangila.webshop.shared.infrastructure.config.RabbitMqConfig.INVENTORY_STREAM_KEY;
import static com.github.mangila.webshop.shared.infrastructure.config.RabbitMqConfig.PRODUCT_STREAM_KEY;

@Service
public class OutboxRabbitProducer {

    private static final Logger log = LoggerFactory.getLogger(OutboxRabbitProducer.class);

    private final JsonMapper jsonMapper;
    private final RegistryService registryService;
    private final Map<String, RabbitStreamTemplateHolder> streamTemplates;
    private final ObservationRegistry observationRegistry;

    public OutboxRabbitProducer(JsonMapper jsonMapper,
                                RegistryService registryService,
                                @Qualifier("productStreamTemplate") RabbitStreamTemplate productStreamTemplate,
                                @Qualifier("inventoryStreamTemplate") RabbitStreamTemplate inventoryStreamTemplate,
                                ObservationRegistry observationRegistry) {
        this.jsonMapper = jsonMapper;
        this.registryService = registryService;
        this.observationRegistry = observationRegistry;
        this.streamTemplates = Map.of(
                ProductRegistryConfig.PRODUCT_DOMAIN_KEY.value(), new RabbitStreamTemplateHolder(productStreamTemplate, PRODUCT_STREAM_KEY),
                InventoryDomainRegistryConfig.INVENTORY_DOMAIN_KEY.value(), new RabbitStreamTemplateHolder(inventoryStreamTemplate, INVENTORY_STREAM_KEY)
        );
    }

    record RabbitStreamTemplateHolder(RabbitStreamTemplate template, String streamName) {

    }

    @EventListener(ApplicationReadyEvent.class)
    public void verifyTemplates() {
        var domains = registryService.domains();
        var errors = new ArrayList<String>();
        for (String key : streamTemplates.keySet()) {
            if (!domains.contains(key)) {
                errors.add(key);
            }
        }
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ApplicationException("RabbitMQ Stream templates not found for domains: '%s'".formatted(errors));
        }
    }

    public CompletableFuture<Boolean> sendToStream(OutboxMessage outboxMessage) {
        var domain = outboxMessage.domain();
        var event = outboxMessage.event();
        var holder = streamTemplates.get(domain);

        if (Objects.isNull(holder)) {
            log.error("No stream template found for domain: {}", domain);
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }

        var template = holder.template;
        template.setProducerCustomizer((_, producerBuilder) -> {
            producerBuilder.filterValue(message -> message.getApplicationProperties()
                    .get(domain)
                    .toString());
        });

        Message message = template.messageBuilder()
                .applicationProperties()
                .entry(domain, event)
                .messageBuilder()
                .addData(jsonMapper.toBytes(outboxMessage))
                .build();

        var observation = Observation.start(holder.streamName.concat(" ").concat("send"), observationRegistry)
                .lowCardinalityKeyValue("domain", domain)
                .lowCardinalityKeyValue("event", event)
                .lowCardinalityKeyValue("stream", holder.streamName);

        return template.send(message)
                .thenApply(result -> {
                    observation.stop();
                    log.info("OutboxMessage sent to RabbitMQ: {}", message);
                    return result;
                })
                .exceptionally(ex -> {
                    observation.error(ex);
                    log.error("OutboxMessage failed to send to RabbitMQ: {}", message);
                    return Boolean.FALSE;
                });
    }
}
