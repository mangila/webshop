package com.github.mangila.webshop.outbox.application.action.query;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxByDomainAndStatusQuery;
import com.github.mangila.webshop.shared.QueryAction;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class FindAllOutboxByDomainAndStatusQueryAction implements QueryAction<FindAllOutboxByDomainAndStatusQuery, List<Outbox>> {

    private final OutboxQueryRepository repository;

    public FindAllOutboxByDomainAndStatusQueryAction(OutboxQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Outbox> execute(@NotNull FindAllOutboxByDomainAndStatusQuery query) {
        return repository.findAllByDomainAndStatus(query);
    }
}
