package com.github.mangila.webshop.backend.event.domain.model;

import com.github.mangila.webshop.backend.event.domain.command.EventSubscribeCommand;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "event_subscriber")
@EntityListeners(AuditingEntityListener.class)
public class EventSubscriber {

    @Id
    private String consumer;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String type;

    @Column(name = "latest_offset",
            nullable = false)
    private Long latestOffset;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updated;

    protected EventSubscriber() {
    }

    private EventSubscriber(String consumer, String topic, String type, Long latestOffset, Instant created, Instant updated) {
        this.consumer = consumer;
        this.topic = topic;
        this.type = type;
        this.latestOffset = latestOffset;
        this.created = created;
        this.updated = updated;
    }

    private EventSubscriber(String consumer, String topic, String type) {
        this(consumer, topic, type, 0L, null, null);
    }

    public static EventSubscriber from(EventSubscribeCommand command) {
        return new EventSubscriber(command.consumer(), command.topic(), command.type());
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getLatestOffset() {
        return latestOffset;
    }

    public void setLatestOffset(Long offset) {
        this.latestOffset = offset;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }
}
