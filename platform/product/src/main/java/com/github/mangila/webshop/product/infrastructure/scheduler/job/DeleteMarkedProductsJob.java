package com.github.mangila.webshop.product.infrastructure.scheduler.job;

import com.github.mangila.webshop.product.application.action.command.DeleteProductCommandAction;
import com.github.mangila.webshop.product.application.action.query.FindProductsByStatusQueryAction;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByStatusQuery;
import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import com.github.mangila.webshop.shared.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public record DeleteMarkedProductsJob(FindProductsByStatusQueryAction findProductsByStatusQueryAction,
                                      DeleteProductCommandAction deleteProductCommandAction) implements SimpleTask<ProductJobKey> {

    private static final Logger log = LoggerFactory.getLogger(DeleteMarkedProductsJob.class);

    @Override
    public ProductJobKey key() {
        return new ProductJobKey("DELETE_MARKED_PRODUCTS");
    }

    @Override
    public void execute() {
        var query = new FindProductByStatusQuery(
                ProductStatusType.MARKED_FOR_DELETION,
                50
        );
        List<Product> products = findProductsByStatusQueryAction.execute(query);
        for (Product product : products) {
            deleteProductCommandAction.tryExecute(new DeleteProductCommand(product.id()))
                    .onSuccess(processed -> log.info("Product: {} was successfully deleted", product.id()))
                    .onFailure(e -> log.error("Failed to delete product {}", product.id(), e));
        }
    }

}
