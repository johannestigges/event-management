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
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final ProtocolService protocolService;

    @GetMapping("")
    public List<Event> getAll() {
        return eventRepository.findAll().stream()
                .map(this::addParticipants)
                .toList();
    }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable Long id) {
        var event = eventRepository.findById(id).orElseThrow(RuntimeException::new);
        return addParticipants(event);
    }

    @PostMapping("")
    @ResponseBody
    public Event create(@RequestBody Event event) {
        var dbEvent = eventRepository.insert(event);
        var participants = event.participants().stream()
                .peek(p -> participantRepository.insert(dbEvent.id(), p.user_id(), p.participate()))
                .map(p -> new Participant(dbEvent.id(), p.user_id(), p.participate()))
                .toList();
        var result = new Event(dbEvent, participants);
        protocolService.newEntity(result.id(), "Event", result);
        return result;
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Event update(@RequestBody Event event, @PathVariable Long id) {
        var dbEvent = eventRepository.findByIdAndVersion(event.id(), event.version())
                .orElseThrow(RuntimeException::new);
        final Event savedEvent = eventRepository.update(event);

        participantRepository.removeFromEvent(event.id());
        var participants = event.participants().stream()
                .peek(p -> participantRepository.insert(event.id(), p.user_id(), p.participate()))
                .map(p -> new Participant(event.id(), p.user_id(), p.participate()))
                .toList();
        var result = new Event(dbEvent, participants);
        protocolService.modifiedEntity(result.id(), "Event", result);
        return result;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        participantRepository.removeFromEvent(id);
        eventRepository.deleteById(id);
        protocolService.deletedEntity(id, "Event");
    }

    @PutMapping("/participants/{id}")
    public void updateParticipant(@RequestBody Participant participant, @PathVariable Long id) {
        participantRepository.update(participant.event_id(), participant.user_id(), participant.participate());
        protocolService.modifiedEntity(id, "Participant", participant);
    }

    public Event updateParticipants(Event event) {
        participantRepository.removeFromEvent(event.id());
        var participants = event.participants().stream()
                .peek(p -> participantRepository.insert(event.id(), p.user_id(), p.participate()))
                .map(p -> new Participant(event.id(), p.user_id(), p.participate()))
                .toList();
        return new Event(event, participants);
    }

    private Event addParticipants(Event event) {
        return new Event(event, participantRepository.findByEventId(event.id()));
    }
}
