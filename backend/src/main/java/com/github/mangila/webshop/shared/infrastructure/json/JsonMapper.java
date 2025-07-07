package com.github.mangila.webshop.shared.infrastructure.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import graphql.Assert;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

@Component
public class JsonMapper {

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode toJsonNode(Object object) {
        return Try.of(() -> {
                    Assert.assertNotNull(object, "Object must not be null");
                    return (JsonNode) objectMapper.valueToTree(object);
                })
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error parsing object: %s", object), cause));
    }

    public <T> T toObject(byte[] bytes, Class<T> clazz) {
        return Try.of(() -> objectMapper.readValue(bytes, clazz))
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error serialize object: %s", clazz.getSimpleName()), cause));
    }

    public byte[] toBytes(Object object) {
        return Try.of(() -> objectMapper.writeValueAsBytes(object))
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error deserialize object: %s", object.getClass().getSimpleName()), cause));
    }
}
