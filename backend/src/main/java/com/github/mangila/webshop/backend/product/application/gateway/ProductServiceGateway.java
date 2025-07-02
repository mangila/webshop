package com.github.mangila.webshop.backend.product.application.gateway;

import com.github.mangila.webshop.backend.product.application.service.ProductDeleteCommandService;
import com.github.mangila.webshop.backend.product.application.service.ProductInsertCommandService;
import com.github.mangila.webshop.backend.product.application.service.ProductQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceGateway {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceGateway.class);

    private final ProductQueryService query;
    private final ProductDeleteCommandService delete;
    private final ProductInsertCommandService insert;

    public ProductServiceGateway(ProductQueryService query,
                                 ProductDeleteCommandService delete,
                                 ProductInsertCommandService insert) {
        this.query = query;
        this.delete = delete;
        this.insert = insert;
    }

    public ProductQueryService query() {
        return query;
    }

    public ProductDeleteCommandService delete() {
        return delete;
    }

    public ProductInsertCommandService insert() {
        return insert;
    }
}
