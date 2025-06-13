package com.github.mangila.webshop.common.model;

public class Notification {

    private final Long id;
    private final String topic;

    public Notification(Long id, String topic) {
        this.id = id;
        this.topic = topic;
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }
}
