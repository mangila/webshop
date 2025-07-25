package com.github.mangila.webshop.identity.application;


import com.github.mangila.webshop.identity.domain.DomainId;
import com.github.mangila.webshop.identity.domain.DomainIdRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DomainIdGeneratorService {

    private final DomainIdRepository repository;

    public DomainIdGeneratorService(DomainIdRepository repository) {
        this.repository = repository;
    }

    public UUID generate(NewDomainIdRequest request) {
        var record = DomainId.create(request.domain());
        return repository.save(record).getId();
    }

    public boolean hasGenerated(UUID id) {
        return repository.existsById(id);
    }
}
