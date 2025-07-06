package com.github.mangila.webshop.shared.uuid.application;

import org.springframework.util.Assert;

public record GenerateNewUuidIntent(String value) {

    public GenerateNewUuidIntent {
        Assert.notNull(value, "value must not be null");
        Assert.hasText(value, "value must not be empty");
    }
}