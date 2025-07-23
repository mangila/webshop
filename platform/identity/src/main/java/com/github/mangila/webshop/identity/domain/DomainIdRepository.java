package com.github.mangila.webshop.identity.domain;

import java.util.Optional;
import java.util.UUID;

public interface DomainIdRepository {

    DomainId save(DomainId record);

    Optional<DomainId> findById(UUID id);

    boolean existsById(UUID id);
}
