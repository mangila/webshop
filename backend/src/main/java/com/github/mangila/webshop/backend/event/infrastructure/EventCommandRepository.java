package com.github.mangila.webshop.backend.event.infrastructure;

import com.github.mangila.webshop.backend.event.domain.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCommandRepository extends JpaRepository<Event, Long> {
}
