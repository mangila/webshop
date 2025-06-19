package com.github.mangila.webshop.backend.common.util;

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

    public JsonNode toJsonNode(Object object) {
        return readTree(object);
    }

    public JsonNode toJsonNode(String json) {
        return readTree(json);
    }

    public boolean isValid(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            return node.isObject();
        } catch (Exception e) {
            return false;
        }
    }

    private JsonNode readTree(Object object) {
        try {
            JsonNode jsonNode = objectMapper.valueToTree(object);
            if (jsonNode.isNull()) {
                return objectMapper.createObjectNode();
            }
            return jsonNode;
        } catch (Exception e) {
            log.error("Error converting object to json: {}", object, e);
            return objectMapper.createObjectNode();
        }
    }

    private JsonNode readTree(String json) {
        try {
            if (!isValid(json)) {
                return objectMapper.createObjectNode();
            }
            return objectMapper.readTree(json);
        } catch (Exception e) {
            log.error("Error converting json to object: {}", json, e);
            return objectMapper.createObjectNode();
        }
    }
}
