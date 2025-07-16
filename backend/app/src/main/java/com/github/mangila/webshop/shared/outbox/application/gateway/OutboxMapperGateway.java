package com.github.mangila.webshop.shared.outbox.application.gateway;

import com.github.mangila.webshop.shared.outbox.application.mapper.OutboxCommandMapper;
import com.github.mangila.webshop.shared.outbox.application.mapper.OutboxDtoMapper;
import com.github.mangila.webshop.shared.outbox.application.mapper.OutboxQueryMapper;
import org.springframework.stereotype.Service;

@Service
public class OutboxMapperGateway {

    private final OutboxCommandMapper command;
    private final OutboxQueryMapper query;
    private final OutboxDtoMapper dto;

    public OutboxMapperGateway(OutboxCommandMapper command,
                               OutboxQueryMapper query,
                               OutboxDtoMapper dto) {
        this.command = command;
        this.query = query;
        this.dto = dto;
    }

    public OutboxCommandMapper command() {
        return command;
    }

    public OutboxQueryMapper query() {
        return query;
    }

    public OutboxDtoMapper dto() {
        return dto;
    }
}
