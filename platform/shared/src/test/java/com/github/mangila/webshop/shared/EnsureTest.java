package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.exception.ApplicationException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnsureTest {

    @Test
    void notNull() {
        assertThatThrownBy(() -> Ensure.notNull(null, "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatNoException()
                .isThrownBy(() -> Ensure.notNull("not null", "no error"));
    }

    @Test
    void notEmpty() {
        assertThatThrownBy(() -> Ensure.notEmpty((byte[]) null, "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatThrownBy(() -> Ensure.notEmpty(new byte[0], "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatNoException()
                .isThrownBy(() -> Ensure.notEmpty(new byte[1], "no error"));
    }

    @Test
    void notBlank() {
        assertThatThrownBy(() -> Ensure.notBlank(null, "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatThrownBy(() -> Ensure.notBlank("   ", "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatNoException()
                .isThrownBy(() -> Ensure.notBlank("not blank", "no error"));
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
        assertThatThrownBy(() -> Ensure.min(1, 0, "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatThrownBy(() -> Ensure.min(1, -1, "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatNoException()
                .isThrownBy(() -> {
                    Ensure.min(1, 1, "no error");
                    Ensure.min(1, 10, "no error");
                });
    }

    @Test
    void max() {
        assertThatThrownBy(() -> Ensure.max(10, 11, "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatThrownBy(() -> Ensure.max(-10, -1, "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatNoException()
                .isThrownBy(() -> {
                    Ensure.max(1, 1, "no error");
                    Ensure.max(10, 1, "no error");
                });
    }

    @Test
    void beforeOrEquals() {
        assertThatThrownBy(() -> Ensure.beforeOrEquals(Instant.now(), Instant.now().minusSeconds(5), "error"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("error");
        assertThatNoException()
                .isThrownBy(() -> {
                    Ensure.beforeOrEquals(Instant.now(), Instant.now().plusSeconds(5), "no error");
                    Ensure.beforeOrEquals(Instant.now(), Instant.now(), "no error");
                });
    }
}