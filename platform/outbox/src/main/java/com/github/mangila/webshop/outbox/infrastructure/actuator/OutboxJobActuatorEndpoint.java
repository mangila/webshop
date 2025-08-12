package com.github.mangila.webshop.outbox.infrastructure.actuator;

import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.OutboxJobKey;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.OutboxJobRunner;
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

@WebEndpoint(id = "outboxjob")
@Component
public class OutboxJobActuatorEndpoint {

    private final Map<OutboxJobKey, SimpleTask<OutboxJobKey>> outboxJobKeyToJob;
    private final OutboxJobRunner outboxJobRunner;

    public OutboxJobActuatorEndpoint(Map<OutboxJobKey, SimpleTask<OutboxJobKey>> outboxJobKeyToJob, OutboxJobRunner outboxJobRunner) {
        this.outboxJobKeyToJob = outboxJobKeyToJob;
        this.outboxJobRunner = outboxJobRunner;
    }

    @ReadOperation
    public WebEndpointResponse<Map<String, List<String>>> findAllJobs() {
        var keys = outboxJobKeyToJob.keySet()
                .stream()
                .map(OutboxJobKey::value)
                .toList();
        return new WebEndpointResponse<>(
                Map.of("jobs", keys),
                HttpStatus.OK.value()
        );
    }

    @WriteOperation
    public WebEndpointResponse<Map<String, Object>> execute(@Selector String key) {
        outboxJobRunner.execute(new OutboxJobKey(key));
        return new WebEndpointResponse<>(HttpStatus.NO_CONTENT.value());
    }
}
