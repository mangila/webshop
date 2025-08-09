package com.github.mangila.webshop.outbox.application.action.query;

import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdByStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.QueryAction;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class FindAllOutboxIdsByStatusQueryAction implements QueryAction<FindAllOutboxIdByStatusQuery, List<OutboxId>> {
    private final OutboxQueryRepository repository;

    public FindAllOutboxIdsByStatusQueryAction(OutboxQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<OutboxId> execute(@NotNull FindAllOutboxIdByStatusQuery query) {
        return repository.findAllIdsByStatus(query);
    }
}
