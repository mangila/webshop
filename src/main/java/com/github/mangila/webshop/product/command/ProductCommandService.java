package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.product.ProductMapper;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommand;
import com.github.mangila.webshop.product.model.ProductCommandException;
import com.github.mangila.webshop.product.model.ProductEntity;
import org.springframework.stereotype.Service;

import static com.github.mangila.webshop.product.model.ProductCommandType.*;

@Service
public class ProductCommandService {

    private final ProductMapper productMapper;
    private final ProductCommandRepository commandRepository;

    public ProductCommandService(ProductMapper productMapper,
                                 ProductCommandRepository commandRepository) {
        this.productMapper = productMapper;
        this.commandRepository = commandRepository;
    }

    public Product updateProductPrice(ProductCommand command) {
        ProductEntity entity = productMapper.toEntity(command);
        return commandRepository.updateOneField(entity, "price")
                .orElseThrow(() -> new ProductCommandException(UPDATE_PRODUCT_PRICE, entity.id()));
    }

    public Product upsert(ProductCommand command) {
        ProductEntity entity = productMapper.toEntity(command);
        return commandRepository.upsertProduct(entity)
                .orElseThrow(() -> new ProductCommandException(UPSERT_PRODUCT, entity.id()));
    }

    public Product delete(ProductCommand command) {
        ProductEntity entity = productMapper.toEntity(command);
        return commandRepository.deleteProductById(entity)
                .orElseThrow(() -> new ProductCommandException(DELETE_PRODUCT, entity.id()));
    }
}
