package com.github.mangila.webshop.backend.event.infrastructure;

import com.github.mangila.webshop.backend.event.domain.model.EventSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventSubscriberRepository extends JpaRepository<EventSubscriber, String> {
}
