package com.github.mangila.webshop.shared;

public interface SimpleTask<K> {

    K key();

    void execute();
}
