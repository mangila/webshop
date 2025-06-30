package com.github.mangila.webshop.backend.product.application;

import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.product.application.service.ProductDeleteCommandService;
import com.github.mangila.webshop.backend.product.application.service.ProductQueryService;
import com.github.mangila.webshop.backend.product.application.service.ProductUpsertCommandService;
import com.github.mangila.webshop.backend.product.domain.query.ProductByIdQuery;
import com.github.mangila.webshop.backend.product.domain.command.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.domain.ProductDomain;
import com.github.mangila.webshop.backend.product.domain.command.ProductUpsertCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceGateway {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceGateway.class);

    private final ProductQueryService queryService;
    private final ProductDeleteCommandService deleteCommandService;
    private final ProductUpsertCommandService upsertCommandService;

    public ProductServiceGateway(ProductQueryService queryService,
                                 ProductDeleteCommandService deleteCommandService,
                                 ProductUpsertCommandService upsertCommandService) {
        this.queryService = queryService;
        this.deleteCommandService = deleteCommandService;
        this.upsertCommandService = upsertCommandService;
    }

    public ProductDomain findById(ProductByIdQuery query) {
        return queryService.findById(query);
    }

    public Event upsert(ProductUpsertCommand command) {
        return upsertCommandService.execute(command);
    }

    public Event delete(ProductDeleteCommand command) {
        return deleteCommandService.execute(command);
    }
}
