package com.github.mangila.webshop.shared.domain.uuid.infrastructure;

import com.github.mangila.webshop.shared.domain.uuid.domain.UuidRecord;
import com.github.mangila.webshop.shared.domain.uuid.domain.UuidRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultUuidRecordEntityRepository implements UuidRecordRepository {

    private final UuidRecordEntityRepository repository;
    private final UuidRecordEntityMapper mapper;

    public DefaultUuidRecordEntityRepository(UuidRecordEntityRepository repository,
                                             UuidRecordEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UuidRecord save(UuidRecord record) {
        var entity = repository.save(mapper.toEntity(record));
        return mapper.toDomain(entity);
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
