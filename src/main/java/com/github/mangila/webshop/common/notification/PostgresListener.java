package com.github.mangila.webshop.common.notification;

public interface PostgresListener {
    void start();
    void shutdown();
    void listen();
}
