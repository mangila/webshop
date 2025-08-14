package com.github.mangila.webshop.shared;

public interface SimpleJobRunner<K> {
    void execute(K key);
}
