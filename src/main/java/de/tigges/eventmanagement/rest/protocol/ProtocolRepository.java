package de.tigges.eventmanagement.rest.protocol;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProtocolRepository extends CrudRepository<Protocol, Long> {

    List<Protocol> findAllByOrderByCreatedAt();
}
