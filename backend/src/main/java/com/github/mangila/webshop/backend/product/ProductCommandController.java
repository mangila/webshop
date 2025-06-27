package com.github.mangila.webshop.backend.product;

import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.command.model.ProductInsertCommand;
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
    public ResponseEntity<Event> insertProduct(@Valid @RequestBody ProductInsertCommand command) {
        return ResponseEntity.ok(productServiceGateway.insert(command));
    }

    @DeleteMapping(
            value = "delete",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> deleteProduct(@Valid @RequestBody ProductDeleteCommand command) {
        return ResponseEntity.ok(productServiceGateway.delete(command));
    }
}
