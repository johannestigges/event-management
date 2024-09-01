package de.tigges.eventmanagement.rest.events;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findAllByOrderByStartAtAsc();

    Optional<Event> findByIdAndVersion(Long id, Long version);
}
