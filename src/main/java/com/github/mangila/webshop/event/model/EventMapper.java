package com.github.mangila.webshop.event.model;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final BeanPropertyRowMapper<Event> rowMapper;

    public EventMapper() {
        this.rowMapper = new BeanPropertyRowMapper<>(Event.class);
    }

    public BeanPropertyRowMapper<Event> getRowMapper() {
        return rowMapper;
    }
}
