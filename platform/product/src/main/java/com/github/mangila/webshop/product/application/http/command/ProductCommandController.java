package com.github.mangila.webshop.product.application.http.command;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.http.request.ProductByIdRequest;
import com.github.mangila.webshop.product.application.http.request.ProductInsertRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/product/command")
public class ProductCommandController {

    private final ProductCommandHttpFacade facade;

    public ProductCommandController(ProductCommandHttpFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public ResponseEntity<ProductDto> insert(@RequestBody ProductInsertRequest request) {
        ProductDto dto = facade.insert(request);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
        facade.deleteById(new ProductByIdRequest(id));
        return ResponseEntity.noContent().build();
    }
}
