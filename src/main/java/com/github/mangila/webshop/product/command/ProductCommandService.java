package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.product.command.model.ProductUpsertCommand;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommandException;
import org.springframework.stereotype.Service;

import static com.github.mangila.webshop.product.command.model.ProductCommandType.DELETE_PRODUCT;
import static com.github.mangila.webshop.product.command.model.ProductCommandType.UPSERT_PRODUCT;

@Service
public class ProductCommandService {

    private final ProductCommandRepository commandRepository;

    public ProductCommandService(ProductCommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Product upsert(ProductUpsertCommand command) {
        return commandRepository.upsert(command)
                .orElseThrow(() -> new ProductCommandException(UPSERT_PRODUCT, command.id()));
    }

    public Product delete(ProductDeleteCommand command) {
        return commandRepository.delete(command)
                .orElseThrow(() -> new ProductCommandException(DELETE_PRODUCT, command.id()));
    }
}
