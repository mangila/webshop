package com.github.mangila.webshop.product.config;

import com.github.mangila.webshop.product.application.action.command.DeleteProductCommandAction;
import com.github.mangila.webshop.product.application.action.query.FindProductsByStatusQueryAction;
import com.github.mangila.webshop.product.infrastructure.task.DeleteProductTask;
import com.github.mangila.webshop.product.infrastructure.task.ProductTaskKey;
import com.github.mangila.webshop.shared.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ProductTaskConfig {

    private static final Logger log = LoggerFactory.getLogger(ProductTaskConfig.class);

    private final FindProductsByStatusQueryAction findProductsByStatusQueryAction;
    private final DeleteProductCommandAction deleteProductCommandAction;

    public ProductTaskConfig(FindProductsByStatusQueryAction findProductsByStatusQueryAction,
                             DeleteProductCommandAction deleteProductCommandAction) {
        this.findProductsByStatusQueryAction = findProductsByStatusQueryAction;
        this.deleteProductCommandAction = deleteProductCommandAction;
    }

    @Bean
    Map<ProductTaskKey, SimpleTask<ProductTaskKey>> keyToProductTask() {
        return Map.ofEntries(
                addTask(new DeleteProductTask(findProductsByStatusQueryAction, deleteProductCommandAction))
        );
    }

    private Map.Entry<ProductTaskKey, SimpleTask<ProductTaskKey>> addTask(SimpleTask<ProductTaskKey> task) {
        log.info("Add Task: {}", task.key());
        return Map.entry(task.key(), task);
    }

    @Bean
    Map<String, ProductTaskKey> nameToProductTaskKey(Map<ProductTaskKey, SimpleTask<ProductTaskKey>> productTasks) {
        return productTasks.keySet()
                .stream()
                .collect(Collectors.toMap(
                        ProductTaskKey::value,
                        Function.identity()
                ));
    }

}
