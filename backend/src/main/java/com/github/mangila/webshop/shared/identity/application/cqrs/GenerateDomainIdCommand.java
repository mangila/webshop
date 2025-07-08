package com.github.mangila.webshop.shared.identity.application.cqrs;

import com.github.mangila.webshop.shared.application.registry.DomainKey;
import jakarta.validation.constraints.NotNull;

public record GenerateDomainIdCommand(@NotNull DomainKey domainKey,
                                      @NotNull String intent) {

    public static GenerateDomainIdCommand from(Class<?> domain, String intent) {
        return new GenerateDomainIdCommand(DomainKey.from(domain), intent);
    }

}