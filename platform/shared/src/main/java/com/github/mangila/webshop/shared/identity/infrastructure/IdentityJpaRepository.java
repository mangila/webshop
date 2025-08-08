package com.github.mangila.webshop.shared.identity.infrastructure;

import com.github.mangila.webshop.shared.identity.domain.Identity;
import com.github.mangila.webshop.shared.identity.domain.IdentityRepository;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public class IdentityJpaRepository implements IdentityRepository {

    private final IdentityEntityRepository repository;
    private final IdentityEntityMapper mapper;

    public IdentityJpaRepository(IdentityEntityRepository repository,
                                 IdentityEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Identity save(Identity record) {
        return Stream.of(record)
                .map(mapper::toEntity)
                .map(repository::save)
                .map(mapper::toDomain)
                .get();
    }

    @Override
    public Optional<Identity> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
