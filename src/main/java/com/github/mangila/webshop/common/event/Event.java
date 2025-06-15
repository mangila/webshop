package com.github.mangila.webshop.common.event;

import java.time.LocalDateTime;

public class Event {

    private Long id;
    private String topic;
    private String type;
    private String aggregateId;
    private String data;
    private LocalDateTime created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", eventType='" + type + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", eventData='" + data + '\'' +
                ", created=" + created +
                '}';
    }
}