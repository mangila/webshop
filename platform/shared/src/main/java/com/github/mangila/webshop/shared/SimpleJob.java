package com.github.mangila.webshop.shared;

public interface SimpleJob<K> {

    K key();

    void execute();
}
