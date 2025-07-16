package com.github.mangila.webshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.types.ProductUnit;
import com.github.mangila.webshop.shared.application.dto.DomainMoneyDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Profile("dev")
public class Bootstrap implements CommandLineRunner {

    private final ObjectMapper objectMapper;

    public Bootstrap(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.ofVirtual().factory().newThread(() -> {
            while (true) {
                try {
                    Thread.sleep(400);
                    var client = HttpClient.newHttpClient();
                    var c = new ProductInsertCommand(
                            "tesst",
                            new DomainMoneyDto("SEK", new BigDecimal(10)),
                            objectMapper.createObjectNode(),
                            ProductUnit.PIECE
                    );
                    client.send(HttpRequest.newBuilder()
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(c)))
                            .uri(java.net.URI.create("http://localhost:8080/api/v1/product/command/insert"))
                            .build(), HttpResponse.BodyHandlers.ofString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
