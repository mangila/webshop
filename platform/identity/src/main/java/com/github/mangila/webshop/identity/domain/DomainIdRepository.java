package com.github.mangila.webshop.identity.domain;

import java.util.UUID;

public interface DomainIdRepository {

    DomainId save(DomainId record);

    DomainId findByIdOrThrow(UUID id);

    boolean existsById(UUID id);
}
