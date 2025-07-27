package com.github.mangila.webshop.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {JsonMapper.class, ObjectMapper.class})
class JsonMapperTest {

    @Autowired
    private JsonMapper jsonMapper;

    record TestObject(String name) {
    }

    @Test
    void toObjectNode() {
        var errorMsg = "Error parsing object";
        assertThatThrownBy(() -> jsonMapper.toObjectNode(null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(errorMsg);
        assertThatThrownBy(() -> jsonMapper.toObjectNode(new Object()))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(errorMsg);
        assertThatThrownBy(() -> jsonMapper.toObjectNode(List.of("test")))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(errorMsg);
        assertThatNoException()
                .isThrownBy(() -> jsonMapper.toObjectNode(new TestObject("test")));
    }
}