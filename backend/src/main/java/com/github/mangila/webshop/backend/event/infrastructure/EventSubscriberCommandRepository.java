package com.github.mangila.webshop.backend.event.infrastructure;

import com.github.mangila.webshop.backend.event.domain.model.EventSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSubscriberCommandRepository extends JpaRepository<EventSubscriber, String> {
}
