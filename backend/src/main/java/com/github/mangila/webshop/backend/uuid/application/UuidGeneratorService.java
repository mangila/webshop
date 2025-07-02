package com.github.mangila.webshop.backend.uuid.application;

import com.github.mangila.webshop.backend.uuid.domain.UuidRecord;
import com.github.mangila.webshop.backend.uuid.infrastructure.UuidRecordRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidGeneratorService {

    private final UuidRecordRepository repository;

    public UuidGeneratorService(UuidRecordRepository repository) {
        this.repository = repository;
    }

    public UUID generate(String intent) {
        var record = new UuidRecord(intent);
        return repository.save(record).getId();
    }
}
