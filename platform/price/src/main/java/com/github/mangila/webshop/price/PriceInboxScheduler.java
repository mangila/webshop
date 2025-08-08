package com.github.mangila.webshop.price;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PriceInboxScheduler {

    @Scheduled(fixedRate = 1000)
    public void abc() {

    }

}
