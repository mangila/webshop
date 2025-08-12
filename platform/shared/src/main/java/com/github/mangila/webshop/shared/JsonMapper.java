package com.github.mangila.webshop.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

/**
 * A utility class for mapping between JSON and Java objects.
 * This class provides methods to convert objects to JSON Nodes and vice versa,
 * leveraging Jackson's ObjectMapper for JSON processing.
 * <p>
 * It ensures that input parameters are non-null and handles any exceptions
 * during the mapping process by wrapping them in a custom ApplicationException.
 * <p>
 * The class is marked as a Spring Component, allowing it to be managed by
 * the Spring container and injected where needed.
 */
@Component
public class JsonMapper {

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectNode toObjectNode(Object object) {
        return Try.of(() -> {
                    Ensure.notNull(object, object.getClass());
                    String json = objectMapper.writeValueAsString(object);
                    return (ObjectNode) objectMapper.readTree(json);
                })
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error parsing object: %s", object), cause));
    }

    public <T> T toObject(ObjectNode node, Class<T> clazz) {
        return Try.of(() -> {
                    Ensure.notNull(node, ObjectNode.class);
                    Ensure.notNull(clazz, clazz);
                    return objectMapper.treeToValue(node, clazz);
                })
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error parsing object: %s", node), cause));
    }
}
