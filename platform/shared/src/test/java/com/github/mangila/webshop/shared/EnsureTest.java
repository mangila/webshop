package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.exception.ApplicationException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnsureTest {

    @Test
    void notNull() {
        assertThatThrownBy(() -> Ensure.notNull(null))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("Object cannot be null");
        assertThatNoException()
                .isThrownBy(() -> Ensure.notNull("not null", String.class));
    }

    @Test
    void notEmpty() {
        assertThatThrownBy(() -> Ensure.notEmpty(null, Object[].class))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("cannot be null");
        assertThatThrownBy(() -> Ensure.notEmpty(new Object[0], Object[].class))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("cannot be empty");
        assertThatNoException()
                .isThrownBy(() -> Ensure.notEmpty(new Object[1], Object[].class));
    }

    @Test
    void notBlank() {
        assertThatThrownBy(() -> Ensure.notBlank(null, String.class))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("cannot be null");
        assertThatThrownBy(() -> Ensure.notBlank("   ", String.class))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("cannot be blank");
        assertThatNoException()
                .isThrownBy(() -> Ensure.notBlank("not blank", String.class));
    }

    @Test
    void isTrue() {
        assertThatThrownBy(() -> Ensure.isTrue(false, "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatNoException()
                .isThrownBy(() -> Ensure.isTrue(true, "no error"));
    }

    @Test
    void min() {
        assertThatThrownBy(() -> Ensure.min(1, 0))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("must be greater than or equal to");
        assertThatThrownBy(() -> Ensure.min(1, -1))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("must be greater than or equal to");
        assertThatNoException()
                .isThrownBy(() -> {
                    Ensure.min(1, 1);
                    Ensure.min(1, 10);
                });
    }

    @Test
    void max() {
        assertThatThrownBy(() -> Ensure.max(10, 11))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("must be less than or equal to");
        assertThatThrownBy(() -> Ensure.max(-10, -1))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("must be less than or equal to");
        assertThatNoException()
                .isThrownBy(() -> {
                    Ensure.max(1, 1);
                    Ensure.max(10, 1);
                });
    }

    @Test
    void beforeOrEquals() {
        assertThatThrownBy(() -> Ensure.beforeOrEquals(Instant.now(), Instant.now().minusSeconds(5)))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("must be before or equal to");
        assertThatNoException()
                .isThrownBy(() -> {
                    Ensure.beforeOrEquals(Instant.now(), Instant.now().plusSeconds(5));
                    Ensure.beforeOrEquals(Instant.now(), Instant.now());
                });
    }
}