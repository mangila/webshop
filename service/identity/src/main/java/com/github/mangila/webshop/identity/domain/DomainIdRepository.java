package com.github.mangila.webshop.identity.domain;

import java.util.UUID;

public interface DomainIdRepository {

    DomainId save(DomainId record);

    DomainId findById(UUID id);

    boolean existsById(UUID id);
}
