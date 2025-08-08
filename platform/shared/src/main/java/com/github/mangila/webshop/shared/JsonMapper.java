package com.github.mangila.webshop.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

@Component
public class JsonMapper {

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectNode toObjectNode(Object object) {
        return Try.of(() -> {
                    Ensure.notNull(object);
                    String json = objectMapper.writeValueAsString(object);
                    return (ObjectNode) objectMapper.readTree(json);
                })
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error parsing object: %s", object), cause));
    }

    public <T> T toObject(ObjectNode node, Class<T> clazz) {
        return Try.of(() -> {
                    Ensure.notNull(node);
                    Ensure.notNull(clazz);
                    return objectMapper.treeToValue(node, clazz);
                })
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error parsing object: %s", node), cause));
    }
}
