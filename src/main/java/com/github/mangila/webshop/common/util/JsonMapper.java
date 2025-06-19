package com.github.mangila.webshop.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class JsonMapper {

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode toJsonNode(Object object) {
        var value = objectMapper.valueToTree(object);
        return value.isNull() ? objectMapper.createObjectNode() : value;
    }

    public JsonNode toJsonNode(String json) {
        try {
            if (isValid(json)) {
                return objectMapper.readTree(json);
            }
            return objectMapper.createObjectNode();
        } catch (Exception e) {
            return objectMapper.createObjectNode();
        }
    }

    public boolean isValid(String json) {
        try {
            if (!StringUtils.hasText(json)) {
                return false;
            } else {
                json = json.trim();
                if (json.startsWith("{") && json.endsWith("}")) {
                    objectMapper.readTree(json);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}
