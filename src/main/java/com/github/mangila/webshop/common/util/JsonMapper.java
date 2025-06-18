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

    public JsonNode toJsonNode(Object object) {
        if (object == null || object == "null") {
            return objectMapper.createObjectNode();
        }
        return objectMapper.valueToTree(object);
    }
}
