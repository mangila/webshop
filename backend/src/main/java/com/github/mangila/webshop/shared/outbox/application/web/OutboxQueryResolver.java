package com.github.mangila.webshop.shared.outbox.application.web;

import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxServiceGateway;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OutboxQueryResolver {

    private final OutboxServiceGateway serviceGateway;

    public OutboxQueryResolver(OutboxServiceGateway serviceGateway) {
        this.serviceGateway = serviceGateway;
    }

    @QueryMapping
    public List<OutboxDto> replay(@Argument("input") @Valid OutboxReplayQuery query) {
        return serviceGateway.query().replay(query);
    }
}
