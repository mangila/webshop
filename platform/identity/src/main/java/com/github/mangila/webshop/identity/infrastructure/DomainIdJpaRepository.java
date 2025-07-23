package com.github.mangila.webshop.identity.infrastructure;

import com.github.mangila.webshop.identity.domain.DomainId;
import com.github.mangila.webshop.identity.domain.DomainIdRepository;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
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
    public Optional<DomainId> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
