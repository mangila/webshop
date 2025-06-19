package com.github.mangila.webshop.backend.product;

import com.github.mangila.webshop.backend.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.command.model.ProductUpsertCommand;
import com.github.mangila.webshop.backend.product.model.Product;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product/command")
public class ProductCommandController {

    private final ProductServiceGateway productServiceGateway;

    public ProductCommandController(ProductServiceGateway productServiceGateway) {
        this.productServiceGateway = productServiceGateway;
    }

    @PostMapping("upsert")
    public ResponseEntity<Product> upsertProduct(@Valid @RequestBody ProductUpsertCommand command) {
        return ResponseEntity.ok(productServiceGateway.upsert(command));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Product> deleteProduct(@Valid @RequestBody ProductDeleteCommand command) {
        return ResponseEntity.ok(productServiceGateway.delete(command));
    }
}
