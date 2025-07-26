package com.github.mangila.webshop.identity.application;


import com.github.mangila.webshop.identity.domain.DomainId;
import com.github.mangila.webshop.identity.domain.DomainIdRepository;
import com.github.mangila.webshop.identity.domain.cqrs.NewDomainIdCommand;
import com.github.mangila.webshop.shared.util.Ensure;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DomainIdService {

    private final DomainIdRepository repository;

    public DomainIdService(DomainIdRepository repository) {
        this.repository = repository;
    }

    public DomainId generate(NewDomainIdCommand command) {
        Ensure.notNull(command, "NewDomainIdCommand must not be null");
        var record = DomainId.create(command.domain());
        return repository.save(record);
    }

    public boolean hasGenerated(UUID id) {
        return repository.existsById(id);
    }
}
