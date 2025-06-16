package com.github.mangila.webshop.event.query;

import com.github.mangila.webshop.event.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventQueryRepository {

    private static final Logger log = LoggerFactory.getLogger(EventQueryRepository.class);

    private final JdbcTemplate jdbc;

    public EventQueryRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Event> replay(String topic, String aggregateId, long offset) {
        return List.of();
    }
}
