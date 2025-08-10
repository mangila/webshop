package com.github.mangila.webshop.product.infrastructure.actuator;

import com.github.mangila.webshop.product.infrastructure.scheduler.job.ProductJobKey;
import com.github.mangila.webshop.product.infrastructure.scheduler.job.ProductJobRunner;
import com.github.mangila.webshop.shared.SimpleTask;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@WebEndpoint(id = "productjob")
@Component
public class ProductJobActuatorEndpoint {

    private final Map<ProductJobKey, SimpleTask<ProductJobKey>> productJobKeyToJob;
    private final ProductJobRunner productJobRunner;

    public ProductJobActuatorEndpoint(Map<ProductJobKey, SimpleTask<ProductJobKey>> productJobKeyToJob,
                                      ProductJobRunner productJobRunner) {
        this.productJobKeyToJob = productJobKeyToJob;
        this.productJobRunner = productJobRunner;
    }

    @ReadOperation
    public WebEndpointResponse<Map<String, List<String>>> findAllTasks() {
        var jobs = productJobKeyToJob.keySet()
                .stream()
                .map(ProductJobKey::value)
                .toList();
        return new WebEndpointResponse<>(
                Map.of("jobs", jobs),
                HttpStatus.OK.value()
        );
    }

    @WriteOperation
    public WebEndpointResponse<Map<String, Object>> execute(@Selector String key) {
        productJobRunner.execute(new ProductJobKey(key));
        return new WebEndpointResponse<>(HttpStatus.NO_CONTENT.value());
    }
}
