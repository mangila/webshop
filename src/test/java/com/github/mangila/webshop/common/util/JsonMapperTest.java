package com.github.mangila.webshop.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {JsonMapper.class, ObjectMapper.class})
class JsonMapperTest {

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void shouldReturnEmptyJsonNodeWhenObjectIsNull() {
        JsonNode node = jsonMapper.toJsonNode(null);
        assertThat(node.isEmpty()).isTrue();
    }

    @Test
    void shouldReturnEmptyJsonNodeWhenStringIsNull() {
        JsonNode node = jsonMapper.toJsonNode("null");
        assertThat(node.isEmpty()).isTrue();
    }
}