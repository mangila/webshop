package com.github.mangila.webshop.shared.identity.domain.cqrs;


import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.Domain;

public record NewIdentityCommand(Domain domain) {
    public NewIdentityCommand {
        Ensure.notNull(domain, NewIdentityCommand.class);
    }
}
