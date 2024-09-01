package de.tigges.eventmanagement.rest.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface InstrumentRepository extends CrudRepository<Instrument, Long> {

    List<Instrument> findAllByOrderByIdAsc();
}
