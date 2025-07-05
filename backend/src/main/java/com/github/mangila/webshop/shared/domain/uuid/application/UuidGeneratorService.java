package com.github.mangila.webshop.shared.domain.uuid.application;

import com.github.mangila.webshop.shared.domain.uuid.domain.UuidRecord;
import com.github.mangila.webshop.shared.domain.uuid.domain.UuidRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

@Service
public class UuidGeneratorService {

    private final UuidRecordRepository repository;

    public UuidGeneratorService(UuidRecordRepository repository) {
        this.repository = repository;
    }

    public UUID generate(GenerateNewUuidCommand command) {
        Assert.notNull(command, String.format("%s must not be null", GenerateNewUuidCommand.class.getSimpleName()));
        var record = UuidRecord.create(command.value());
        return repository.save(record).getId();
    }

    public boolean hasGenerated(UUID id) {
        return repository.existsById(id);
    }
}
