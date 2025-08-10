package com.github.mangila.webshop.product.infrastructure.task;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SimpleTask;
import com.github.mangila.webshop.shared.SimpleTaskRunner;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductSimpleTaskRunner implements SimpleTaskRunner<ProductTaskKey> {

    private final Map<String, ProductTaskKey> nameToProductTaskKey;
    private final Map<ProductTaskKey, SimpleTask<ProductTaskKey>> keyToProductTask;

    public ProductSimpleTaskRunner(Map<String, ProductTaskKey> nameToProductTaskKey,
                                   Map<ProductTaskKey, SimpleTask<ProductTaskKey>> keyToProductTask) {
        this.nameToProductTaskKey = nameToProductTaskKey;
        this.keyToProductTask = keyToProductTask;
    }

    @Override
    public void execute(ProductTaskKey key) {
        Ensure.notNull(key, ProductTaskKey.class);
        var task = keyToProductTask.get(key);
        Ensure.notNull(task, () -> new ResourceNotFoundException(
                ProductTaskKey.class,
                key
        ));
        task.execute();
    }
}
