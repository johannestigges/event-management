package de.tigges.eventmanagement.rest.users.jpa;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface InstrumentRepository extends CrudRepository<InstrumentEntity, Long> {
    List<InstrumentEntity> findAll(Sort Sort);
}
