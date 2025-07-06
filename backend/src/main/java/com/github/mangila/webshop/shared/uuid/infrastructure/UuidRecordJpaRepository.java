package com.github.mangila.webshop.shared.uuid.infrastructure;

import com.github.mangila.webshop.shared.uuid.domain.UuidRecord;
import com.github.mangila.webshop.shared.uuid.domain.UuidRecordRepository;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
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
    public Optional<UuidRecord> findById(UUID id) {
        var entity = repository.findById(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
