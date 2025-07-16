package com.github.mangila.webshop.shared.identity.application;

import com.github.mangila.webshop.shared.identity.application.cqrs.GenerateDomainIdCommand;
import com.github.mangila.webshop.shared.identity.domain.DomainId;
import com.github.mangila.webshop.shared.identity.domain.DomainIdRepository;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@ObservedService
public class DomainIdGeneratorService {

    private final DomainIdRepository repository;

    public DomainIdGeneratorService(DomainIdRepository repository) {
        this.repository = repository;
    }

    public UUID generate(@Valid GenerateDomainIdCommand command) {
        var record = DomainId.create(command.domain(), command.intent());
        return repository.save(record).getId();
    }

    public boolean hasGenerated(UUID id) {
        return repository.existsById(id);
    }
}
