package com.github.mangila.webshop.identity.infrastructure;

import com.github.mangila.webshop.identity.domain.DomainId;
import com.github.mangila.webshop.identity.domain.DomainIdRepository;
import com.github.mangila.webshop.shared.exception.CqrsException;
import com.github.mangila.webshop.shared.model.CqrsOperation;
import io.vavr.collection.Stream;

import java.util.UUID;


@ObservedRepository
public class DomainIdJpaRepository implements DomainIdRepository {

    private final DomainIdEntityRepository repository;
    private final DomainIdEntityMapper mapper;

    public DomainIdJpaRepository(DomainIdEntityRepository repository,
                                 DomainIdEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DomainId save(DomainId record) {
        return Stream.of(record)
                .map(mapper::toEntity)
                .map(repository::save)
                .map(mapper::toDomain)
                .get();
    }

    @Override
    public DomainId findById(UUID id) {
        var entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new CqrsException(
                    String.format("UuidRecord not found for id: %s", id),
                    CqrsOperation.QUERY,
                    DomainId.class);
        }
        return mapper.toDomain(entity.get());
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
