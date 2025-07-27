package com.github.mangila.webshop.identity.domain.cqrs;


import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.Domain;

public record NewDomainIdCommand(Domain domain) {

    public NewDomainIdCommand {
        Ensure.notNull(domain, "Domain must not be null");
    }

}
