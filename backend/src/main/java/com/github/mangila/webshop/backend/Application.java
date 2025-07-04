package com.github.mangila.webshop.backend;

import com.github.mangila.webshop.backend.outboxevent.domain.OutboxEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RabbitListener(queues = "event.stream", containerFactory = "c1")
    void listen(OutboxEvent event) {
        System.out.println(event);
    }
}
