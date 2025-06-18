package com.github.mangila.webshop.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class JsonMapper {

    private static final Logger log = LoggerFactory.getLogger(JsonMapper.class);

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @NotNull
    public JsonNode toJsonNode(@Nullable Object object) {
        var value = objectMapper.valueToTree(object);
        return value.isNull() ? objectMapper.createObjectNode() : value;
    }

    @NotNull
    public JsonNode toJsonNode(@Nullable String json) {
        try {
            if (!StringUtils.hasText(json)) {
                return objectMapper.createObjectNode();
            }
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
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
