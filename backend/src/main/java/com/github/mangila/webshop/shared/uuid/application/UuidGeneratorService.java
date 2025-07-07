package com.github.mangila.webshop.shared.uuid.application;

import com.github.mangila.webshop.shared.uuid.domain.UuidRecord;
import com.github.mangila.webshop.shared.uuid.domain.UuidRecordRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

@Service
public class UuidGeneratorService {

    private final UuidRecordRepository repository;

    public UuidGeneratorService(UuidRecordRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "domain-id", allEntries = true)
    public UUID generate(GenerateNewUuidIntent intent) {
        Assert.notNull(intent, "GenerateNewUuidIntent must not be null");
        var record = UuidRecord.create(intent.value());
        return repository.save(record).getId();
    }

    @Cacheable(value = "domain-id")
    public boolean hasGenerated(UUID id) {
        return repository.existsById(id);
    }
}
