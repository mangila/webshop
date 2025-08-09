package com.github.mangila.webshop.shared;

public interface SimpleTask<K> {

    void execute();

    K key();
}
