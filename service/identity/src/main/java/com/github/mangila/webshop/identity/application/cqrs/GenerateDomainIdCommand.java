package com.github.mangila.webshop.identity.application.cqrs;

import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.infrastructure.spring.validation.RegistredDomain;
import jakarta.validation.constraints.NotNull;

public record GenerateDomainIdCommand(@NotNull @RegistredDomain Domain domain,
                                      @NotNull String intent) {

    public static GenerateDomainIdCommand from(Class<?> domain, String intent) {
        return new GenerateDomainIdCommand(Domain.from(domain), intent);
    }

}