package com.github.mangila.webshop.identity.domain;

import java.util.Optional;
import java.util.UUID;

public interface IdentityRepository {

    Identity save(Identity record);

    Optional<Identity> findById(UUID id);

    boolean existsById(UUID id);
}
