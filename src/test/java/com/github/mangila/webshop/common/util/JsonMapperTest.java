package com.github.mangila.webshop.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    }

    @Test
    @DisplayName("Should return empty JsonNode when String is 'null' and null")
    void shouldReturnEmptyJsonNodeWhenStringIsNull() {
        JsonNode node = jsonMapper.toJsonNode("null");
        assertThat(node.isEmpty()).isTrue();
        node = jsonMapper.toJsonNode((String) null);
        assertThat(node.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should return empty JsonNode when String is Blank and Empty")
    void shouldReturnEmptyJsonNodeWhenStringIsBlank() {
        JsonNode node = jsonMapper.toJsonNode(" ");
        assertThat(node.isEmpty()).isTrue();
        node = jsonMapper.toJsonNode("");
        assertThat(node.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should return false when String is null")
    void shouldReturnFalseWhenStringIsNull() {
        assertThat(jsonMapper.isValid(null))
                .isFalse();
    }

    @Test
    @DisplayName("Should return false when String is Blank")
    void shouldReturnFalseWhenStringIsBlank() {
        assertThat(jsonMapper.isValid(" ")).isFalse();
    }

    @Test
    @DisplayName("Should return false when String is Empty")
    void shouldReturnFalseWhenStringIsEmpty() {
        assertThat(jsonMapper.isValid("")).isFalse();
    }

    @Test
    @DisplayName("Should return false when String is not JSON")
    void shouldReturnFalseWhenStringIsNotJson() {
        assertThat(jsonMapper.isValid("{NOT JSON}")).isFalse();
    }
}