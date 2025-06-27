package com.github.mangila.webshop.backend.product.command;

import com.github.mangila.webshop.backend.common.exception.CommandException;
import com.github.mangila.webshop.backend.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.command.model.ProductInsertCommand;
import com.github.mangila.webshop.backend.product.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandService {

    private final ProductCommandRepository commandRepository;

    public ProductCommandService(ProductCommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Product insert(ProductInsertCommand command) {
        var result = commandRepository.insert(command);
        return result.orElseThrow(() -> new CommandException(
                command.getClass(),
                Product.class,
                HttpStatus.CONFLICT,
                "Insert failed"));
    }

    public Product delete(ProductDeleteCommand command) {
        var result = commandRepository.delete(command);
        return result.orElseThrow(() -> new CommandException(
                command.getClass(),
                Product.class,
                HttpStatus.NOT_FOUND,
                String.format("id not found: '%s'", command.id())));
    }
}
