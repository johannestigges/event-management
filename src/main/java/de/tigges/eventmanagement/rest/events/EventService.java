package de.tigges.eventmanagement.rest.events;

import java.util.Set;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.tigges.eventmanagement.rest.events.jpa.EventEntity;
import de.tigges.eventmanagement.rest.events.jpa.EventRepository;
import de.tigges.eventmanagement.rest.protocol.ProtocolService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/events")
@RequiredArgsConstructor
public class EventService {
    private final EventRepository repository;
    private final ProtocolService protocolService;

    @GetMapping("")
    Set<Event> getAll() {
        return EventMapper.mapEntities(repository.findAll());
    }

    @GetMapping("/{id}")
    Event getOne(@PathVariable Long id) {
        return repository.findById(id)
                .map(e -> EventMapper.mapEntity(e))
                .orElseThrow(() -> new RuntimeException());
    }

    @PostMapping("")
    @ResponseBody
    Event create(@RequestBody Event event) {
        EventEntity entity = EventMapper.map(event);
        entity = repository.save(entity);
        protocolService.newEntity(entity.getId(), "Event", entity);
        return EventMapper.mapEntity(entity);
    }

    @PutMapping("/{id}")
    @ResponseBody
    Event update(@RequestBody Event event, @PathVariable Long id) {
        EventEntity entity = EventMapper.map(event);
        repository.save(entity);
        protocolService.modifiedEntity(entity.getId(), "Event", entity);
        return EventMapper.mapEntity(entity);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
        protocolService.deletedEntity(id, "Event");

    }
}
