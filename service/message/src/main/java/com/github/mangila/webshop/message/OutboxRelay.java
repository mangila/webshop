package com.github.mangila.webshop.message;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class OutboxRelay {

    @Transactional
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    void poll() {
        // var outboxMessages = repository.query().findAllByPublished(false, 10);
    }

}
