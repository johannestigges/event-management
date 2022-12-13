package de.tigges.eventmanagement.rest.events.jpa;

import org.springframework.data.repository.CrudRepository;

public interface ParticipantRepository extends CrudRepository<ParticipantEntity, Long> {
}
