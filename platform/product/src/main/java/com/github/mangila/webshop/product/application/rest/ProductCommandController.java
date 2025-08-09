package com.github.mangila.webshop.product.application.rest;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.rest.request.CreateProductRequest;
import com.github.mangila.webshop.product.application.rest.request.UpdateProductStatusRequest;
import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/product/command")
public class ProductCommandController {

    private final ProductRestCommandFacade facade;

    public ProductCommandController(ProductRestCommandFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public ResponseEntity<ProductDto> insert(@RequestBody CreateProductRequest request) {
        ProductDto dto = facade.insert(request);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
        facade.updateStatus(new UpdateProductStatusRequest(
                id,
                ProductStatusType.MARKED_FOR_DELETION
        ));
        return ResponseEntity.noContent().build();
    }
}
