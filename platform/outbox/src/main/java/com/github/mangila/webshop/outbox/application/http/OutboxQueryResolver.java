package com.github.mangila.webshop.outbox.application.http;

import com.github.mangila.webshop.outbox.application.OutboxDto;
import com.github.mangila.webshop.outbox.application.http.request.OutboxReplayRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OutboxQueryResolver {

    private final OutboxQueryHttpFacade webFacade;

    public OutboxQueryResolver(OutboxQueryHttpFacade webFacade) {
        this.webFacade = webFacade;
    }

    @QueryMapping
    public List<OutboxDto> replay(@Argument("request") OutboxReplayRequest request) {
        return webFacade.replay(request);
    }
}
