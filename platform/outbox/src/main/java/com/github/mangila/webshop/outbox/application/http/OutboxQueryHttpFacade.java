package com.github.mangila.webshop.outbox.application.http;

import com.github.mangila.webshop.outbox.application.OutboxDto;
import com.github.mangila.webshop.outbox.application.mapper.OutboxDtoMapper;
import com.github.mangila.webshop.outbox.application.service.OutboxQueryService;
import com.github.mangila.webshop.outbox.application.http.request.OutboxReplayRequest;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class OutboxQueryHttpFacade {

    private final OutboxHttpRequestMapper requestMapper;
    private final OutboxQueryService service;
    private final OutboxDtoMapper outboxDtoMapper;

    public OutboxQueryHttpFacade(OutboxHttpRequestMapper requestMapper,
                                 OutboxQueryService service,
                                 OutboxDtoMapper outboxDtoMapper) {
        this.requestMapper = requestMapper;
        this.service = service;
        this.outboxDtoMapper = outboxDtoMapper;
    }

    public List<OutboxDto> replay(@Valid OutboxReplayRequest request) {
        OutboxReplayQuery query = requestMapper.toQuery(request);
        return service.replay(query)
                .stream()
                .map(outboxDtoMapper::toDto)
                .toList();
    }
}
