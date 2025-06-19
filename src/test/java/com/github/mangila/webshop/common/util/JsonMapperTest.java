package com.github.mangila.webshop.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        JsonMapper.class,
        ObjectMapper.class})
class JsonMapperTest {

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    @DisplayName("Should return empty JsonNode when Object is null")
    void shouldReturnEmptyJsonNodeWhenObjectIsNull() {
        JsonNode node = jsonMapper.toJsonNode((Object) null);
        assertThat(node.isEmpty()).isTrue();
        assertThat(node).isInstanceOf(ObjectNode.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("Should return empty JsonNode for null, empty or blank strings")
    void shouldReturnEmptyJsonNodeForInvalidStrings(String input) {
        JsonNode node = jsonMapper.toJsonNode(input);
        assertThat(node.isEmpty()).isTrue();
        assertThat(node).isInstanceOf(ObjectNode.class);
    }

    @Test
    @DisplayName("Should convert valid JSON string to JsonNode")
    void shouldConvertValidJsonStringToJsonNode() {
        String json = "{\"name\":\"test\",\"value\":123}";
        JsonNode node = jsonMapper.toJsonNode(json);

        assertThat(node.isEmpty()).isFalse();
        assertThat(node.get("name").asText()).isEqualTo("test");
        assertThat(node.get("value").asInt()).isEqualTo(123);
    }

    @Test
    @DisplayName("Should convert object to JsonNode")
    void shouldConvertObjectToJsonNode() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "test");
        map.put("value", 123);

        JsonNode node = jsonMapper.toJsonNode(map);

        assertThat(node.isEmpty()).isFalse();
        assertThat(node.get("name").asText()).isEqualTo("test");
        assertThat(node.get("value").asInt()).isEqualTo(123);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "{NOT JSON}", "invalid"})
    @DisplayName("Should return false for invalid JSON strings")
    void shouldReturnFalseForInvalidJsonStrings(String input) {
        assertThat(jsonMapper.isValid(input)).isFalse();
    }

    @Test
    @DisplayName("Should return true for valid JSON object")
    void shouldReturnTrueForValidJsonObject() {
        assertThat(jsonMapper.isValid("{\"name\":\"test\"}")).isTrue();
    }

    @Test
    @DisplayName("Should return false for JSON array (not object)")
    void shouldReturnFalseForJsonArray() {
        assertThat(jsonMapper.isValid("[1,2,3]")).isFalse();
    }
}
