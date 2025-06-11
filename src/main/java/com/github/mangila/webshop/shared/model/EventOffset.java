package com.github.mangila.webshop.shared.model;

import java.util.Objects;

public class EventOffset {

    private String consumer;
    private String topic;
    private Long offset;

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    // Composite primary key,
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventOffset that = (EventOffset) o;
        return Objects.equals(consumer, that.consumer) &&
                Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consumer, topic);
    }
}