package com.github.mangila.webshop.common;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.event.EventListener;

public interface AbstractEventListener<T> extends DisposableBean {

    @EventListener
    void onEvent(T event);

    void handleException(Exception e, T event);

    void shutdown();
}
