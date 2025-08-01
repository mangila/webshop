package com.github.mangila.webshop.outbox.application.graphql;

import com.github.mangila.webshop.outbox.application.OutboxDto;
import com.github.mangila.webshop.outbox.application.graphql.input.OutboxReplayInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OutboxQueryResolver {

    private final OutboxQueryFacade webFacade;

    public OutboxQueryResolver(OutboxQueryFacade webFacade) {
        this.webFacade = webFacade;
    }

    @QueryMapping
    public List<OutboxDto> replay(@Argument("input") OutboxReplayInput input) {
        return webFacade.replay(input);
    }
}
