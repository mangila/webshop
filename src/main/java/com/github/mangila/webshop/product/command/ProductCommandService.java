package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.product.ProductMapper;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEntity;
import com.github.mangila.webshop.product.model.ProductMutate;
import com.github.mangila.webshop.product.model.exception.ProductCommandException;
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

    public Product updateProductPrice(ProductMutate mutate) {
        var id = mutate.id();
        String fieldName = "price";
        var price = mutate.price();
        return commandRepository.updateOneField(id, fieldName, price)
                .orElseThrow(() -> new ProductCommandException(UPDATE_PRODUCT_PRICE, id));
    }

    public Product upsert(ProductMutate mutate) {
        ProductEntity entity = productMapper.toEntity(mutate);
        return commandRepository.upsertProduct(entity)
                .orElseThrow(() -> new ProductCommandException(UPSERT_PRODUCT, mutate.id()));
    }

    public Product delete(ProductMutate mutate) {
        var id = mutate.id();
        return commandRepository.deleteProductById(id)
                .orElseThrow(() -> new ProductCommandException(DELETE_PRODUCT, id));
    }
}
