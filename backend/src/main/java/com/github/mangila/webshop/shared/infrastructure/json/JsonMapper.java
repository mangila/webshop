package com.github.mangila.webshop.shared.infrastructure.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
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
