package com.github.mangila.webshop.backend.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.backend.common.util.exception.ApiException;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class JsonMapper {

    private static final Logger log = LoggerFactory.getLogger(JsonMapper.class);

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode toJsonNode(Object object) {
        return readTree(object);
    }

    public JsonNode toJsonNode(String json) {
        return readTree(json);
    }

    public boolean isValid(String json) {
        return Try.of(() -> {
                    JsonNode node = readTree(json);
                    return node.isObject();
                })
                .getOrElse(() -> {
                    log.error("Invalid json: {}", json);
                    return Boolean.FALSE;
                });
    }

    private JsonNode readTree(Object object) {
        try {
            JsonNode jsonNode = tryReadTree(object);
            if (jsonNode.isNull()) {
                return objectMapper.createObjectNode();
            }
            return jsonNode;
        } catch (Exception e) {
            log.error("Error parsing object: {}", object, e);
            var message = e.getMessage();
            var cause = e.getCause();
            throw new ApiException(message, JsonMapper.class, HttpStatus.CONFLICT, cause);
        }
    }

    private JsonNode readTree(String json) {
        try {
            return tryReadTree(json);
        } catch (Exception e) {
            log.error("Error parsing json: {}", json, e);
            var message = e.getMessage();
            var cause = e.getCause();
            throw new ApiException(message, JsonMapper.class, HttpStatus.CONFLICT, cause);
        }
    }

    private JsonNode tryReadTree(Object object) {
        return objectMapper.valueToTree(object);
    }

    private JsonNode tryReadTree(String json) throws JsonProcessingException {
        return objectMapper.readTree(json);
    }
}
