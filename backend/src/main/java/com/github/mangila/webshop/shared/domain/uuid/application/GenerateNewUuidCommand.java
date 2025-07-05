package com.github.mangila.webshop.shared.domain.uuid.application;

import org.springframework.util.Assert;

public record GenerateNewUuidCommand(String value) {

    public GenerateNewUuidCommand {
        Assert.notNull(value, "value must not be null");
        Assert.hasText(value, "value must not be empty");
    }
}
