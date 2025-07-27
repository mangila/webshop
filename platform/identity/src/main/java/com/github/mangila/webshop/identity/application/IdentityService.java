package com.github.mangila.webshop.identity.application;


import com.github.mangila.webshop.identity.domain.Identity;
import com.github.mangila.webshop.identity.domain.IdentityRepository;
import com.github.mangila.webshop.identity.domain.cqrs.NewIdentityCommand;
import com.github.mangila.webshop.shared.Ensure;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdentityService {

    private final IdentityRepository repository;

    public IdentityService(IdentityRepository repository) {
        this.repository = repository;
    }

    public Identity generate(NewIdentityCommand command) {
        Ensure.notNull(command, "NewIdentityCommand must not be null");
        var record = Identity.createNew(command.domain());
        return repository.save(record);
    }

    public boolean hasGenerated(UUID id) {
        return repository.existsById(id);
    }
}
