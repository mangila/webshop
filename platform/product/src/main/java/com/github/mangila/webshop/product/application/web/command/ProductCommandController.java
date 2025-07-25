package com.github.mangila.webshop.product.application.web.command;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.web.request.ProductInsertRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/product/command")
public class ProductCommandController {

    private final ProductCommandWebFacade webFacade;

    public ProductCommandController(ProductCommandWebFacade webFacade) {
        this.webFacade = webFacade;
    }

    @PostMapping
    public ResponseEntity<ProductDto> insert(@RequestBody ProductInsertRequest request) {
        ProductDto dto = webFacade.insert(request);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
        webFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
