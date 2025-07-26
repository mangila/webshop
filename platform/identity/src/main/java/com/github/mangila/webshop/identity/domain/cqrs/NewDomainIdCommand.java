package com.github.mangila.webshop.identity.domain.cqrs;


import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.util.Ensure;

public record NewDomainIdCommand(Domain domain) {

    public NewDomainIdCommand {
        Ensure.notNull(domain, "Domain must not be null");
    }

}
