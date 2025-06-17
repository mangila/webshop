package com.github.mangila.webshop.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JsonMapper {

    private static final Logger log = LoggerFactory.getLogger(JsonMapper.class);

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode emptyJsonNode() {
        return objectMapper.createObjectNode();
    }

    public JsonNode toJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            log.error("Failed to parse json: {}", json, e);
            return emptyJsonNode();
        }
    }

    public JsonNode toJsonNode(Object object) {
        return objectMapper.valueToTree(object);
    }

    public String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Failed to serialize object: {}", object, e);
            return null;
        }
    }

    public <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("Failed to parse json: {}", json, e);
            return null;
        }
    }
}
