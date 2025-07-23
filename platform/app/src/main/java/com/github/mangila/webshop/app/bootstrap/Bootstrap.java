package com.github.mangila.webshop.app.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.product.application.web.request.ProductInsertRequest;
import com.github.mangila.webshop.product.domain.types.ProductUnit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
                    var request = new ProductInsertRequest(
                            "test",
                            objectMapper.createObjectNode(),
                            ProductUnit.PIECE
                    );
                    client.send(HttpRequest.newBuilder()
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(request)))
                            .uri(java.net.URI.create("http://localhost:8080/api/v1/product/command"))
                            .build(), HttpResponse.BodyHandlers.ofString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
