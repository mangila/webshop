package com.github.mangila.webshop.backend.product.command;

import com.github.mangila.webshop.backend.common.util.exception.RequestedResourceNotFoundException;
import com.github.mangila.webshop.backend.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.command.model.ProductUpsertCommand;
import com.github.mangila.webshop.backend.product.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandService {

    private final ProductCommandRepository commandRepository;

    public ProductCommandService(ProductCommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Product upsert(ProductUpsertCommand command) {
        var result = commandRepository.upsert(command);
        return result.orElseThrow(() -> new RequestedResourceNotFoundException(
                Product.class,
                String.format("id not found: '%s'", command.id())));
    }

    public Product delete(ProductDeleteCommand command) {
        var result = commandRepository.delete(command);
        return result.orElseThrow(() -> new RequestedResourceNotFoundException(
                Product.class,
                String.format("id not found: '%s'", command.id())));
    }
}
