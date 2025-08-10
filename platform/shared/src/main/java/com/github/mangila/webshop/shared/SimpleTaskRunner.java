package com.github.mangila.webshop.shared;

public interface SimpleTaskRunner<K> {
    void execute(K key);
}
