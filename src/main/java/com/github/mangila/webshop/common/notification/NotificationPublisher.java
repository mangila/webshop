package com.github.mangila.webshop.common.notification;

import com.github.mangila.webshop.common.notification.model.Notification;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisher {

    private final ApplicationEventPublisher publisher;

    public NotificationPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishNotification(Notification notification) {
        publisher.publishEvent(notification);
    }
}
