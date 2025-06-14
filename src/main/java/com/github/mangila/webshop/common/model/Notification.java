package com.github.mangila.webshop.common.model;

public abstract class Notification {

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

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                '}';
    }
}
