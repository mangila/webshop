package com.github.mangila.webshop.product.infrastructure.scheduler.job;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SimpleJob;
import com.github.mangila.webshop.shared.SimpleJobRunner;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductJobRunner implements SimpleJobRunner<ProductJobKey> {
    private final Map<ProductJobKey, SimpleJob<ProductJobKey>> keyToProductTask;

    public ProductJobRunner(Map<ProductJobKey, SimpleJob<ProductJobKey>> keyToProductTask) {
        this.keyToProductTask = keyToProductTask;
    }

    @Override
    public void execute(ProductJobKey key) {
        Ensure.notNull(key, ProductJobKey.class);
        var task = keyToProductTask.get(key);
        Ensure.notNull(task, () -> new ResourceNotFoundException(
                ProductJobKey.class,
                key
        ));
        task.execute();
    }
}
