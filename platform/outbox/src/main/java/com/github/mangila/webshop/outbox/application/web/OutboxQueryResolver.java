package com.github.mangila.webshop.outbox.application.web;

import com.github.mangila.webshop.outbox.application.OutboxDto;
import com.github.mangila.webshop.outbox.application.web.request.OutboxReplayRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OutboxQueryResolver {

    private final OutboxQueryWebFacade webFacade;

    public OutboxQueryResolver(OutboxQueryWebFacade webFacade) {
        this.webFacade = webFacade;
    }

    @QueryMapping
    public List<OutboxDto> replay(@Argument("request") OutboxReplayRequest request) {
        return webFacade.replay(request);
    }
}
