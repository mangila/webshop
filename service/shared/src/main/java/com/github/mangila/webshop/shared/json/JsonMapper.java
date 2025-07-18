package com.github.mangila.webshop.shared.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import io.vavr.control.Try;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class JsonMapper {

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectNode toObjectNode(Object object) {
        return Try.of(() -> {
                    Assert.notNull(object, "Object cannot be null");
                    String json = objectMapper.writeValueAsString(object);
                    return (ObjectNode) objectMapper.readTree(json);
                })
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error parsing object: %s", object), cause));
    }

    public <T> T toObject(byte[] bytes, Class<T> clazz) {
        return Try.of(() -> {
                    Assert.notNull(bytes, "Array cannot be null");
                    Assert.isTrue(!ArrayUtils.isEmpty(bytes), "Array cannot be empty");
                    return objectMapper.readValue(bytes, clazz);
                })
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error serialize object: %s", clazz.getSimpleName()), cause));
    }

    public byte[] toBytes(Object object) {
        return Try.of(() -> {
                    Assert.notNull(object, "Object cannot be null");
                    return objectMapper.writeValueAsBytes(object);
                })
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error deserialize object: %s", object.getClass().getSimpleName()), cause));
    }
}
