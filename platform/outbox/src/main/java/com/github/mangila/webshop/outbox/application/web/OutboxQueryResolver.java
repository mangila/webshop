package com.github.mangila.webshop.outbox.application.web;

import com.github.mangila.webshop.outbox.application.OutboxDto;
import com.github.mangila.webshop.outbox.application.web.request.OutboxReplayRequest;
import io.micrometer.observation.ObservationRegistry;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OutboxQueryResolver {

    private final OutboxQueryWebFacade webFacade;
    private final ObservationRegistry observationRegistry;

    public OutboxQueryResolver(OutboxQueryWebFacade webFacade, ObservationRegistry observationRegistry) {
        this.webFacade = webFacade;
        this.observationRegistry = observationRegistry;
    }

    @QueryMapping
    public List<OutboxDto> replay(@Argument("request") @Valid OutboxReplayRequest request) {
        return webFacade.replay(request);
    }
}
