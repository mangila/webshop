package com.github.mangila.webshop.shared;

public interface SimpleTaskRunner<K> {
    K findKey(String taskKey);

    void execute(K taskKey);
}
