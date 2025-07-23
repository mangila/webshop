package com.github.mangila.webshop.product.application.web.command;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.web.request.ProductInsertRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/product/command")
public class ProductCommandController {

    private final ProductCommandWebService webService;

    public ProductCommandController(ProductCommandWebService webService) {
        this.webService = webService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> insert(@Valid @RequestBody ProductInsertRequest request) {
        ProductDto dto = webService.insert(request);
        return ResponseEntity.ok(dto);
    }
}
