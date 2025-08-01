package com.github.mangila.webshop.outbox.application.graphql;

import com.github.mangila.webshop.outbox.application.OutboxDto;
import com.github.mangila.webshop.outbox.application.mapper.OutboxDtoMapper;
import com.github.mangila.webshop.outbox.application.service.OutboxQueryService;
import com.github.mangila.webshop.outbox.application.graphql.input.OutboxReplayInput;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class OutboxQueryFacade {

    private final OutboxInputMapper requestMapper;
    private final OutboxQueryService service;
    private final OutboxDtoMapper outboxDtoMapper;

    public OutboxQueryFacade(OutboxInputMapper requestMapper,
                             OutboxQueryService service,
                             OutboxDtoMapper outboxDtoMapper) {
        this.requestMapper = requestMapper;
        this.service = service;
        this.outboxDtoMapper = outboxDtoMapper;
    }

    public List<OutboxDto> replay(@Valid OutboxReplayInput request) {
        OutboxReplayQuery query = requestMapper.toQuery(request);
        return service.replay(query)
                .stream()
                .map(outboxDtoMapper::toDto)
                .toList();
    }
}
