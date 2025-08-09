package com.github.mangila.webshop.outbox.application.graphql;

import org.springframework.stereotype.Controller;

@Controller
public class OutboxQueryResolver {

    private final OutboxQueryFacade facade;

    public OutboxQueryResolver(OutboxQueryFacade facade) {
        this.facade = facade;
    }

}
