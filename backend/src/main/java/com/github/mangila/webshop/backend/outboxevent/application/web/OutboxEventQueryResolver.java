package com.github.mangila.webshop.backend.outboxevent.application.web;

import com.github.mangila.webshop.backend.outboxevent.domain.OutboxEvent;
import com.github.mangila.webshop.backend.outboxevent.domain.query.OutboxEventReplayQuery;
import com.github.mangila.webshop.backend.outboxevent.application.gateway.OutboxEventServiceGateway;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OutboxEventQueryResolver {

    private final OutboxEventServiceGateway serviceGateway;

    public OutboxEventQueryResolver(OutboxEventServiceGateway serviceGateway) {
        this.serviceGateway = serviceGateway;
    }

    @QueryMapping
    public List<OutboxEvent> replay(@Argument("input") @Valid OutboxEventReplayQuery query) {
        return serviceGateway.query().replay(query);
    }

}
