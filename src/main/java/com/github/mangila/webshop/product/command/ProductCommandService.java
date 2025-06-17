package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.common.util.Pair;
import com.github.mangila.webshop.event.command.EventCommandService;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventTopic;
import com.github.mangila.webshop.product.ProductEventType;
import com.github.mangila.webshop.product.ProductMapper;
import com.github.mangila.webshop.product.ProductValidator;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommandType;
import com.github.mangila.webshop.product.model.ProductMutate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandService.class);

    private final ProductMapper productMapper;
    private final JsonMapper jsonMapper;
    private final EventCommandService eventCommandService;
    private final ProductCommandRepository commandRepository;
    private final ProductValidator validator;

    public ProductCommandService(ProductMapper productMapper,
                                 JsonMapper jsonMapper,
                                 EventCommandService eventCommandService,
                                 ProductCommandRepository commandRepository,
                                 ProductValidator validator) {
        this.productMapper = productMapper;
        this.jsonMapper = jsonMapper;
        this.eventCommandService = eventCommandService;
        this.commandRepository = commandRepository;
        this.validator = validator;
    }

    @Transactional
    public Product processMutateCommand(ProductCommandType command, ProductMutate mutate) {
        log.info("Processing command -- {} -- {}", command, mutate);
        var product = productMapper.toProduct(mutate);
        var pair = switch (command) {
            case UPSERT_PRODUCT -> upsertProduct(product);
            case DELETE_PRODUCT -> deleteProduct(product);
            case UPDATE_PRODUCT_PRICE -> updateProductPrice(product);
        };
        if (pair.isEmpty()) {
            return null;
        }
        product = pair.first();
        var event = pair.second();
        log.info("Processed command -- {} -- {} -- {}", command, product, event);
        return product;
    }

    private Pair<Product, Event> upsertProduct(Product product) {
        validator.ensureRequiredFields(product);
        var result = commandRepository.upsertProduct(
                productMapper.toEntity(product)
        );
        if (result.isPresent()) {
            product = result.get();
            var event = eventCommandService.emit(
                    EventTopic.PRODUCT,
                    product.getId(),
                    ProductEventType.PRODUCT_UPSERTED.toString(),
                    jsonMapper.toJsonNode(product)
            );
            return Pair.of(product, event);
        }
        return Pair.empty();
    }

    private Pair<Product, Event> deleteProduct(Product product) {
        validator.ensureProductId(product);
        var id = product.getId();
        var result = commandRepository.deleteProductById(id);
        if (result.isPresent()) {
            product = result.get();
            var event = eventCommandService.emit(
                    EventTopic.PRODUCT,
                    product.getId(),
                    ProductEventType.PRODUCT_DELETED.toString(),
                    jsonMapper.toJsonNode(product)
            );
            return Pair.of(product, event);
        }
        return Pair.empty();
    }

    private Pair<Product, Event> updateProductPrice(Product product) {
        validator.ensurePrice(product);
        var result = commandRepository.updateOneField(
                product.getId(),
                "price",
                product.getPrice());
        var event = eventCommandService.emit(
                EventTopic.PRODUCT,
                product.getId(),
                ProductEventType.PRODUCT_PRICE_UPDATED.toString(),
                jsonMapper.toJsonNode(product)
        );
        return Pair.of(product, event);
    }
}
