package com.github.mangila.webshop.product.infrastructure.actuator;

import com.github.mangila.webshop.product.infrastructure.task.ProductTaskKey;
import com.github.mangila.webshop.product.infrastructure.task.ProductSimpleTaskRunner;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@WebEndpoint(id = "producttask")
@Component
public class ProductTaskActuatorEndpoint {

    private final Map<String, ProductTaskKey> nameToProductTaskKey;
    private final ProductSimpleTaskRunner productSimpleTaskRunner;

    public ProductTaskActuatorEndpoint(Map<String, ProductTaskKey> nameToProductTaskKey, ProductSimpleTaskRunner productSimpleTaskRunner) {
        this.nameToProductTaskKey = nameToProductTaskKey;
        this.productSimpleTaskRunner = productSimpleTaskRunner;
    }

    @ReadOperation
    public WebEndpointResponse<Map<String, Set<String>>> findAllTasks() {
        return new WebEndpointResponse<>(
                Map.of("tasks", nameToProductTaskKey.keySet()),
                HttpStatus.OK.value()
        );
    }

    @WriteOperation
    public WebEndpointResponse<Map<String, Object>> execute(@Selector String key) {
        productSimpleTaskRunner.execute(new ProductTaskKey(key));
        return new WebEndpointResponse<>(HttpStatus.NO_CONTENT.value());
    }
}
