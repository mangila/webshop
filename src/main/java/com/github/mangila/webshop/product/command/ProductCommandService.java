package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.common.event.EventService;
import com.github.mangila.webshop.common.event.EventTopic;
import com.github.mangila.webshop.product.ProductEventType;
import com.github.mangila.webshop.product.ProductValidator;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommandType;
import com.github.mangila.webshop.product.query.ProductQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandService.class);

    private final EventService eventService;
    private final ProductQueryService queryService;
    private final ProductCommandRepository commandRepository;
    private final ProductValidator validator;

    public ProductCommandService(EventService eventService,
                                 ProductQueryService queryService,
                                 ProductCommandRepository commandRepository,
                                 ProductValidator validator) {
        this.eventService = eventService;
        this.queryService = queryService;
        this.commandRepository = commandRepository;
        this.validator = validator;
    }

    @Transactional
    public Product processCommand(ProductCommandType command, Product product) {
        return switch (command) {
            case UPSERT_PRODUCT -> upsertProduct(product);
            case DELETE_PRODUCT -> deleteProduct(product);
            case UPDATE_PRODUCT_PRICE -> updateProductPrice(product);
        };
    }

    private Product upsertProduct(Product product) {
        validator.ensureRequiredFields(product);
        var result = commandRepository.upsertProduct(product);
        eventService.emit(
                EventTopic.PRODUCT,
                result.getId(),
                ProductEventType.PRODUCT_UPSERTED.toString(),
                result
        );
        log.debug("Product upserted -- {}", result);
        return result;
    }

    private Product deleteProduct(Product product) {
        validator.ensureProductId(product);
        var id = product.getId();
        var result = queryService.queryById(id);
        commandRepository.deleteProductById(id);
        eventService.emit(
                EventTopic.PRODUCT,
                result.getId(),
                ProductEventType.PRODUCT_DELETED.toString(),
                result
        );
        log.debug("Product deleted -- {}", result);
        return result;
    }

    private Product updateProductPrice(Product product) {
        validator.ensurePrice(product);
        var update = commandRepository.update(product.getId(), product.getPrice(), "price");
        eventService.emit(
                EventTopic.PRODUCT,
                update.getId(),
                ProductEventType.PRODUCT_PRICE_UPDATED.toString(),
                update
        );
        log.debug("Product price updated -- {}", update);
        return update;
    }
}
