package com.github.mangila.webshop.common;

import com.github.mangila.webshop.common.model.Notification;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

public interface AbstractNotificationListener<T extends Notification> extends DisposableBean {

    @EventListener(ApplicationReadyEvent.class)
    void onReady();

    @EventListener
    void onNotification(T notification);

    void handleException(Exception e, T notification);

    void shutdown();
}
