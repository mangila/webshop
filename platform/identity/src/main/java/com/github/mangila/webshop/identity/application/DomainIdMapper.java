package com.github.mangila.webshop.identity.application;

import com.github.mangila.webshop.identity.domain.cqrs.NewDomainIdCommand;
import org.springframework.stereotype.Component;

@Component
public class DomainIdMapper {
    public NewDomainIdCommand toCommand(NewDomainIdRequest request) {
        return new NewDomainIdCommand(request.domain());
    }
}
