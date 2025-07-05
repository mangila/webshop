package com.github.mangila.webshop.product.application.web;

import com.github.mangila.webshop.outboxevent.domain.OutboxEvent;
import com.github.mangila.webshop.product.application.gateway.ProductServiceGateway;
import com.github.mangila.webshop.product.domain.command.ProductDeleteCommand;
import com.github.mangila.webshop.product.domain.command.ProductInsertCommand;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product/command")
public class ProductCommandController {

    private final ProductServiceGateway productServiceGateway;

    public ProductCommandController(ProductServiceGateway productServiceGateway) {
        this.productServiceGateway = productServiceGateway;
    }

    @PostMapping(
            value = "insert",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OutboxEvent> insertProduct(@Valid @RequestBody ProductInsertCommand command) {
        return ResponseEntity.ok(productServiceGateway.command().insert(command));
    }

    @DeleteMapping(
            value = "delete",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OutboxEvent> deleteProduct(@Valid @RequestBody ProductDeleteCommand command) {
        return ResponseEntity.ok(productServiceGateway.command().delete(command));
    }
}
