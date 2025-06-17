package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.product.ProductMapper;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEntity;
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

    public Product updateProductPrice(Product product) {
        var id = product.getId();
        String fieldName = "price";
        var price = product.getPrice();
        return commandRepository.updateOneField(id, fieldName, price)
                .orElseThrow(() -> new ProductCommandException(UPDATE_PRODUCT_PRICE, id));
    }

    public Product upsert(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        return commandRepository.upsertProduct(entity)
                .orElseThrow(() -> new ProductCommandException(UPSERT_PRODUCT, product.getId()));
    }

    public Product delete(Product product) {
        var id = product.getId();
        return commandRepository.deleteProductById(id)
                .orElseThrow(() -> new ProductCommandException(DELETE_PRODUCT, id));
    }
}
