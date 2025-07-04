package com.github.mangila.webshop.backend.common.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.backend.common.domain.exception.ApiException;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JsonMapper {

    private static final Logger log = LoggerFactory.getLogger(JsonMapper.class);

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode toJsonNode(Object object) {
        return readTree(object);
    }

    private JsonNode readTree(Object object) {
        return Try.of(() -> {
                    JsonNode jsonNode = objectMapper.valueToTree(object);
                    if (jsonNode.isNull()) {
                        return objectMapper.createObjectNode();
                    }
                    return jsonNode;
                })
                .getOrElseThrow(() -> new ApiException("Error parsing object: " + object, JsonMapper.class, HttpStatus.CONFLICT));
    }
}
