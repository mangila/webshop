package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.exception.ApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistinctQueueTest {

    @Test
    @DisplayName("Should fill the Queue with strings")
    void shouldFill() {
        var list = List.of("One", "One", "Two");
        final DistinctQueue<String> queue = new DistinctQueue<>();
        queue.fill(list);
        assertThat(queue.size())
                .isEqualTo(2);
        assertThat(queue.poll())
                .isEqualTo("One");
        assertThat(queue.poll())
                .isEqualTo("Two");
        assertThat(queue.poll())
                .isNull();
    }

    @Test
    @DisplayName("Should be distinct")
    void shouldBeDistinct() {
        final DistinctQueue<String> queue = new DistinctQueue<>();
        queue.add("One");
        queue.add("One");
        queue.add("Two");
        assertThat(queue.size())
                .isEqualTo(2);
        assertThat(queue.poll())
                .isEqualTo("One");
        assertThat(queue.poll())
                .isEqualTo("Two");
        assertThat(queue.poll())
                .isNull();
    }

    @Test
    @DisplayName("Add Null Should throw ApplicationException")
    void addNullShouldThrowException() {
        final DistinctQueue<String> queue = new DistinctQueue<>();
        assertThatThrownBy(() -> queue.add(null))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("Object cannot be null");
    }
}