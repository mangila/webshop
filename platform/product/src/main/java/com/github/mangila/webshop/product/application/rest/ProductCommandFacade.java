package com.github.mangila.webshop.product.application.rest;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.ProductDtoMapper;
import com.github.mangila.webshop.product.application.rest.request.CreateProductRequest;
import com.github.mangila.webshop.product.application.rest.request.DeleteProductRequest;
import com.github.mangila.webshop.product.application.service.CreateProductAction;
import com.github.mangila.webshop.product.application.service.DeleteProductAction;
import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;
import com.github.mangila.webshop.shared.JsonMapper;
import com.github.mangila.webshop.shared.model.CacheName;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductCommandFacade {

    private final ProductRestMapper restMapper;
    private final ProductDtoMapper dtoMapper;

    private final JsonMapper jsonMapper;
    private final CreateProductAction createProductAction;
    private final DeleteProductAction deleteProductAction;

    public ProductCommandFacade(ProductRestMapper restMapper,
                                ProductDtoMapper dtoMapper,
                                JsonMapper jsonMapper,
                                CreateProductAction createProductAction,
                                DeleteProductAction deleteProductAction) {
        this.restMapper = restMapper;
        this.dtoMapper = dtoMapper;
        this.jsonMapper = jsonMapper;
        this.createProductAction = createProductAction;
        this.deleteProductAction = deleteProductAction;
    }

    @Transactional
    @CachePut(value = CacheName.LRU, key = "#result.id()")
    public ProductDto insert(@Valid CreateProductRequest request) {
        CreateProductCommand command = restMapper.toCommand(request);
        OutboxEvent outboxEvent = createProductAction.execute(command);
        return jsonMapper.toObject(outboxEvent.payload(), ProductDto.class);
    }

    @Transactional
    @CacheEvict(value = CacheName.LRU, key = "#request.value()")
    public void deleteById(@Valid DeleteProductRequest request) {
        DeleteProductCommand command = restMapper.toCommand(request);
        deleteProductAction.execute(command);
    }
}
