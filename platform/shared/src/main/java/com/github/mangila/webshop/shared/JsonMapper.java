package com.github.mangila.webshop.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import io.vavr.control.Try;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class JsonMapper {

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectNode toObjectNode(@NotNull Object object) {
        return Try.of(() -> {
                    String json = objectMapper.writeValueAsString(object);
                    return (ObjectNode) objectMapper.readTree(json);
                })
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error parsing object: %s", object), cause));
    }

    public <T> T toObject(@NotNull ObjectNode node, @NotNull Class<T> clazz) {
        return Try.of(() -> objectMapper.treeToValue(node, clazz))
                .getOrElseThrow(cause -> new ApplicationException(String.format("Error parsing object: %s", node), cause));
    }
}
