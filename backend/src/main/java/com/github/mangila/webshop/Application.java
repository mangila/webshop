package com.github.mangila.webshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.ProductUnit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
//        Thread.ofVirtual().factory().newThread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(400);
//                    var client = HttpClient.newHttpClient();
//                    var c = new ProductInsertCommand(
//                            "tesst",
//                            new BigDecimal(10),
//                            objectMapper.createObjectNode(),
//                            ProductUnit.PIECE
//                    );
//                    client.send(HttpRequest.newBuilder()
//                            .header("Content-Type", "application/json")
//                            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(c)))
//                            .uri(java.net.URI.create("http://localhost:8080/api/v1/product/command/insert"))
//                            .build(), HttpResponse.BodyHandlers.ofString());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
    }
}
