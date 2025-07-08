package com.github.mangila.webshop.shared.uuid.infrastructure;

import com.github.mangila.webshop.shared.domain.common.CqrsOperation;
import com.github.mangila.webshop.shared.domain.exception.CqrsException;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedRepository;
import com.github.mangila.webshop.shared.uuid.domain.UuidRecord;
import com.github.mangila.webshop.shared.uuid.domain.UuidRecordRepository;
import io.vavr.collection.Stream;

import java.util.UUID;


@ObservedRepository
public class UuidRecordJpaRepository implements UuidRecordRepository {

    private final UuidRecordEntityRepository repository;
    private final UuidRecordEntityMapper mapper;

    public UuidRecordJpaRepository(UuidRecordEntityRepository repository,
                                   UuidRecordEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UuidRecord save(UuidRecord record) {
        return Stream.of(record)
                .map(mapper::toEntity)
                .map(repository::save)
                .map(mapper::toDomain)
                .get();
    }

    @Override
    public UuidRecord findById(UUID id) {
        var entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new CqrsException("UuidRecord not found for id: " + id, CqrsOperation.QUERY, UuidRecord.class);
        }
        return mapper.toDomain(entity.get());
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
