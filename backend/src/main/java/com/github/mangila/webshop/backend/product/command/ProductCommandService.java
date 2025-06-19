package com.github.mangila.webshop.backend.product.command;

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
        return commandRepository.upsert(command).orElseThrow();
    }

    public Product delete(ProductDeleteCommand command) {
        return commandRepository.delete(command).orElseThrow();
    }
}
