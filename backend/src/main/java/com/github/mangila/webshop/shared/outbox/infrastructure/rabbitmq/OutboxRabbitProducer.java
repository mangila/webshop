package com.github.mangila.webshop.shared.outbox.infrastructure.rabbitmq;

import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;
import com.github.mangila.webshop.shared.outbox.infrastructure.message.OutboxMessage;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Message;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.github.mangila.webshop.shared.infrastructure.config.RabbitMqConfig.INVENTORY_STREAM_KEY;
import static com.github.mangila.webshop.shared.infrastructure.config.RabbitMqConfig.PRODUCT_STREAM_KEY;

@ObservedService
public class OutboxRabbitProducer {

    private static final Logger log = LoggerFactory.getLogger(OutboxRabbitProducer.class);

    private final JsonMapper jsonMapper;
    private final RegistryService registryService;
    private final Map<Domain, RabbitStreamTemplateHolder> streamTemplates;
    private final ObservationRegistry observationRegistry;

    public OutboxRabbitProducer(
            Environment streamEnvironment,
            JsonMapper jsonMapper,
            RegistryService registryService,
            ObservationRegistry observationRegistry) {
        this.jsonMapper = jsonMapper;
        this.registryService = registryService;
        this.observationRegistry = observationRegistry;
        this.streamTemplates = Map.of(
                Domain.from("PRODUCT", registryService), createStreamTemplate(streamEnvironment, PRODUCT_STREAM_KEY),
                Domain.from("INVENTORY", registryService), createStreamTemplate(streamEnvironment, INVENTORY_STREAM_KEY)
        );
    }

    private record RabbitStreamTemplateHolder(RabbitStreamTemplate template, String streamName) {
    }

    private RabbitStreamTemplateHolder createStreamTemplate(Environment streamEnvironment, String streamKey) {
        var template = new RabbitStreamTemplate(streamEnvironment, streamKey);
        template.setObservationEnabled(Boolean.TRUE);
        template.setProducerCustomizer((_, builder) -> {
            builder.filterValue(message -> message.getApplicationProperties()
                    .get("domain")
                    .toString());
            builder.filterValue(message -> message.getApplicationProperties()
                    .get("event")
                    .toString());
            builder.filterValue(message -> message.getApplicationProperties()
                    .get("aggregateId")
                    .toString());
        });
        return new RabbitStreamTemplateHolder(template, streamKey);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void verifyTemplates() {
        var domains = registryService.domains();
        var errors = new ArrayList<Domain>();
        for (Domain key : streamTemplates.keySet()) {
            if (!domains.contains(key)) {
                errors.add(key);
            }
        }
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ApplicationException("RabbitMQ Stream templates not found for registred domains: '%s'".formatted(errors));
        }
    }

    public CompletableFuture<Boolean> sendToStream(OutboxMessage outboxMessage) {
        var domain = Domain.from(outboxMessage.domain(), registryService);
        var event = Event.from(outboxMessage.event(), registryService);
        var holder = streamTemplates.get(domain);

        if (Objects.isNull(holder)) {
            log.error("No stream template found for domain: {}", domain);
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
        var template = holder.template;

        Message rabbitMessage = template.messageBuilder()
                .applicationProperties()
                .entry("domain", domain.value())
                .entry("event", event.value())
                .entry("aggregateId", outboxMessage.aggregateId().toString())
                .messageBuilder()
                .addData(jsonMapper.toBytes(outboxMessage))
                .build();

        var observation = observationRegistry.getCurrentObservation();
        if (Objects.nonNull(observation)) {
            observation.lowCardinalityKeyValue("domain", domain.value())
                    .lowCardinalityKeyValue("event", event.value())
                    .lowCardinalityKeyValue("aggregateId", outboxMessage.aggregateId().toString())
                    .lowCardinalityKeyValue("stream", holder.streamName);
        }

        return template.send(rabbitMessage);
    }
}
