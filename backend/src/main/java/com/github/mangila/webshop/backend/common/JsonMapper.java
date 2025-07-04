package com.github.mangila.webshop.backend.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.backend.common.error.exception.ApiException;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
                .getOrElseThrow(() -> new ApiException(
                        String.format("Error parsing object: %s", object),
                        JsonMapper.class));
    }

    public <T> T toObject(byte[] bytes, Class<T> clazz) {
        return Try.of(() -> objectMapper.readValue(bytes, clazz))
                .getOrElseThrow(throwable ->
                        new ApiException("Error deserialize object", clazz, throwable)
                );
    }

    public byte[] toBytes(Object object) {
        return Try.of(() -> objectMapper.writeValueAsBytes(object))
                .getOrElseThrow(throwable ->
                        new ApiException(
                                String.format("Error serialize object: %s", object),
                                JsonMapper.class,
                                throwable)
                );
    }
}
