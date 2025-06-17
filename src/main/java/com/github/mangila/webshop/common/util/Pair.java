package com.github.mangila.webshop.common.util;

public record Pair<A, B>(A first, B second) {

    private static final Pair<Object, Object> EMPTY = Pair.of(null, null);

    @SuppressWarnings("unchecked")
    public static <A, B> Pair<A, B> empty() {
        return (Pair<A, B>) EMPTY;
    }

    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    public boolean isEmpty() {
        return first == null && second == null;
    }
}
