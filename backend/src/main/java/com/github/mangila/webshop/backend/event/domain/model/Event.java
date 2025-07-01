
package com.github.mangila.webshop.backend.event.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.model.ApplicationUuid;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "event")
@EntityListeners(AuditingEntityListener.class)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventTopic topic;

    @Embedded
    @AttributeOverride(name = "value",
            column = @Column(
                    name = "event_type",
                    nullable = false))
    private EventType eventType;

    @Embedded
    @AttributeOverride(name = "value",
            column = @Column(
                    name = "aggregate_id",
                    nullable = false))
    private ApplicationUuid aggregateId;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "event_data",
            columnDefinition = "jsonb",
            nullable = false)
    private JsonNode eventData;

    @CreatedDate
    private Instant created;

    protected Event() {
    }

    public Event(EventTopic topic, EventType eventType, ApplicationUuid aggregateId, JsonNode eventData) {
        this(null, topic, eventType, aggregateId, eventData, null);
    }

    private Event(Long id, EventTopic topic, EventType eventType, ApplicationUuid aggregateId, JsonNode eventData, Instant created) {
        this.id = id;
        this.topic = topic;
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.eventData = eventData;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public EventTopic getTopic() {
        return topic;
    }

    public EventType getEventType() {
        return eventType;
    }

    public ApplicationUuid getAggregateId() {
        return aggregateId;
    }

    public JsonNode getEventData() {
        return eventData;
    }

    public Instant getCreated() {
        return created;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTopic(EventTopic topic) {
        this.topic = topic;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setAggregateId(ApplicationUuid aggregateId) {
        this.aggregateId = aggregateId;
    }

    public void setEventData(JsonNode eventData) {
        this.eventData = eventData;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}