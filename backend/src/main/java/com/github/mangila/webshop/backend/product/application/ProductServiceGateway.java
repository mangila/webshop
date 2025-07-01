package com.github.mangila.webshop.backend.product.application;

import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.product.application.service.ProductDeleteCommandService;
import com.github.mangila.webshop.backend.product.application.service.ProductQueryService;
import com.github.mangila.webshop.backend.product.application.service.ProductInsertCommandService;
import com.github.mangila.webshop.backend.product.domain.command.ProductInsertCommand;
import com.github.mangila.webshop.backend.product.domain.query.ProductByIdQuery;
import com.github.mangila.webshop.backend.product.domain.command.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceGateway {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceGateway.class);

    private final ProductQueryService queryService;
    private final ProductDeleteCommandService deleteCommandService;
    private final ProductInsertCommandService insertCommandService;

    public ProductServiceGateway(ProductQueryService queryService,
                                 ProductDeleteCommandService deleteCommandService,
                                 ProductInsertCommandService insertCommandService) {
        this.queryService = queryService;
        this.deleteCommandService = deleteCommandService;
        this.insertCommandService = insertCommandService;
    }

    public Product findById(ProductByIdQuery query) {
        return queryService.findById(query);
    }

    public Event insert(ProductInsertCommand command) {
        return insertCommandService.execute(command);
    }

    public Event delete(ProductDeleteCommand command) {
        return deleteCommandService.execute(command);
    }
}
