package de.tigges.eventmanagement.rest.events;

import de.tigges.eventmanagement.rest.protocol.ProtocolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/events")
@RequiredArgsConstructor
@Log4j2
public class EventService {
    private final EventRepository repository;
    private final ParticipantRepository participantRepository;
    private final ProtocolService protocolService;

    @GetMapping("")
    public List<Event> getAll() {
        var events = repository.findAll();
        events.forEach(this::addParticipants);
        return events;
    }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable Long id) {
        var event = repository.findById(id).orElseThrow(RuntimeException::new);
        addParticipants(event);
        return event;
    }

    @PostMapping("")
    @ResponseBody
    public Event create(@RequestBody Event event) {
        var dbEvent = repository.insert(event);
        event.getParticipants().forEach(p -> {
            participantRepository.insert(dbEvent.getId(), p.getUser_id(), p.getParticipate());
            dbEvent.getParticipants().add(Participant.builder()
                    .event_id(dbEvent.getId())
                    .user_id(p.getUser_id())
                    .participate(p.getParticipate())
                    .build());
        });
        protocolService.newEntity(dbEvent.getId(), "Event", dbEvent);
        return dbEvent;
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Event update(@RequestBody Event event, @PathVariable Long id) {
        var dbEvent = repository.findByIdAndVersion(event.getId(), event.getVersion())
                .orElseThrow(RuntimeException::new);
        final Event savedEvent = repository.update(event);
        participantRepository.removeFromEvent(event.getId());
        event.getParticipants().forEach(p -> {
            participantRepository.insert(event.getId(), p.getUser_id(), p.getParticipate());
            savedEvent.getParticipants().add(Participant.builder()
                    .event_id(event.getId())
                    .user_id(p.getUser_id())
                    .participate(p.getParticipate())
                    .build());
        });
        protocolService.modifiedEntity(event.getId(), "Event", savedEvent);
        return savedEvent;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        participantRepository.removeFromEvent(id);
        repository.deleteById(id);
        protocolService.deletedEntity(id, "Event");
    }

    @PutMapping("/participants/{id}")
    public void updateParticipant(@RequestBody Participant participant, @PathVariable Long id) {
        participantRepository.update(participant.getEvent_id(), participant.getUser_id(), participant.getParticipate());
        protocolService.modifiedEntity(id, "Participant", participant);
    }

    private void addParticipants(Event event) {
        event.getParticipants().addAll(participantRepository.findByEventId(event.getId()));
    }
}
