package com.github.mangila.webshop.product.infrastructure.scheduler.task;

import com.github.mangila.webshop.product.application.action.command.DeleteProductCommandAction;
import com.github.mangila.webshop.product.application.action.query.FindProductsByStatusQueryAction;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByStatusQuery;
import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteProductTask implements ProductTask {

    private static final Logger log = LoggerFactory.getLogger(DeleteProductTask.class);
    private final FindProductsByStatusQueryAction findProductsByStatusQueryAction;
    private final DeleteProductCommandAction deleteProductCommandAction;

    public DeleteProductTask(FindProductsByStatusQueryAction findProductsByStatusQueryAction,
                             DeleteProductCommandAction deleteProductCommandAction) {
        this.findProductsByStatusQueryAction = findProductsByStatusQueryAction;
        this.deleteProductCommandAction = deleteProductCommandAction;
    }

    @Override
    public void execute() {
        var query = new FindProductByStatusQuery(
                ProductStatusType.MARKED_FOR_DELETION,
                50
        );
        List<Product> products = findProductsByStatusQueryAction.query(query);
        for (Product product : products) {
            Try.run(() -> deleteProductCommandAction.execute(new DeleteProductCommand(product.id())))
                    .onFailure(e -> log.error("Failed to delete product {}", product.id()));
        }
    }

    @Override
    public ProductTaskKey key() {
        return new ProductTaskKey("DELETE_PRODUCT");
    }

}
