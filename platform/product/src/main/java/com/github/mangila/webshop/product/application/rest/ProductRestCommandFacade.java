package com.github.mangila.webshop.product.application.rest;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.action.command.CreateProductCommandAction;
import com.github.mangila.webshop.product.application.action.command.UpdateProductStatusCommandAction;
import com.github.mangila.webshop.product.application.rest.request.CreateProductRequest;
import com.github.mangila.webshop.product.application.rest.request.DeleteProductRequest;
import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.UpdateProductStatusCommand;
import com.github.mangila.webshop.shared.JsonMapper;
import com.github.mangila.webshop.shared.model.CacheName;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductRestCommandFacade {

    private final ProductRestMapper restMapper;
    private final JsonMapper jsonMapper;
    private final CreateProductCommandAction createProductCommandAction;
    private final UpdateProductStatusCommandAction updateProductStatusCommandAction;

    public ProductRestCommandFacade(ProductRestMapper restMapper,
                                    JsonMapper jsonMapper,
                                    CreateProductCommandAction createProductCommandAction,
                                    UpdateProductStatusCommandAction updateProductStatusCommandAction) {
        this.restMapper = restMapper;
        this.jsonMapper = jsonMapper;
        this.createProductCommandAction = createProductCommandAction;
        this.updateProductStatusCommandAction = updateProductStatusCommandAction;
    }

    @Transactional
    @CachePut(value = CacheName.LRU, key = "#result.id()")
    public ProductDto insert(@Valid CreateProductRequest request) {
        CreateProductCommand command = restMapper.toCommand(request);
        return createProductCommandAction.execute()
                .andThen(outboxEvent -> jsonMapper.toObject(outboxEvent.payload(), ProductDto.class))
                .apply(command);
    }

    @Transactional
    @CacheEvict(value = CacheName.LRU, key = "#request.value()")
    public void deleteById(@Valid DeleteProductRequest request) {
        UpdateProductStatusCommand command = restMapper.toCommand(request);
        updateProductStatusCommandAction.execute(command);
    }
}