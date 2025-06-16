package com.github.mangila.webshop.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public final class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    public static final String EMPTY_JSON = "{}";

    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> Optional<T> deserialize(String payload,
                                              Class<T> type,
                                              ObjectMapper objectMapper) {
        try {
            return Optional.ofNullable(objectMapper.readValue(payload, type));
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize payload: {}", payload, e);
            return Optional.empty();
        }
    }

    public static <T> T ensureDeserialize(String payload,
                                          Class<T> type,
                                          ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(payload, type);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize payload: {}", payload, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> String ensureSerialize(T payload, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize payload: {}", payload, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> String serialize(T payload, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize payload: {}", payload, e);
            return EMPTY_JSON;
        }
    }

}
