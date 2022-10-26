package de.tigges.eventmanagement.rest.events.jpa;

import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<EventEntity, Long> {
}
