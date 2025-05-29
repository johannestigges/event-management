package de.tigges.eventmanagement.rest.events;

import de.tigges.eventmanagement.rest.protocol.ProtocolService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/events")
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final ProtocolService protocolService;

    @GetMapping("")
    public List<Event> getAll() {
        return eventRepository.findAllByOrderByStartAtAsc().stream()
                .map(this::addParticipants)
                .toList();
    }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(this::addParticipants)
                .orElseThrow(() -> new EmptyResultDataAccessException(
                        "event with id %s not found".formatted(id), 1));
    }

    @PostMapping("")
    @ResponseBody
    public Event insert(@RequestBody Event event) {
        return Optional.of(event)
                .map(this::assertNew)
                .map(eventRepository::save)
                .map(savedEvent -> insertParticipants(savedEvent, event.participants()))
                .map(protocolService::newEntity)
                .orElseThrow();
    }

    @PutMapping("")
    @ResponseBody
    public Event update(@RequestBody Event event) {
        return Optional.of(event)
                .map(this::assertEventVersion)
                .map(eventRepository::save)
                .map(savedEvent -> this.updateParticipants(savedEvent, event.participants()))
                .map(protocolService::modifiedEntity)
                .orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        var entity = eventRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(
                        "event with id %d does not exist".formatted(id), 1));
        participantRepository.removeFromEvent(id);
        eventRepository.deleteById(id);
        protocolService.deletedEntity(entity);
    }

    @PutMapping("/participants/{id}")
    public void updateParticipant(@RequestBody Participant participant, @PathVariable Long id) {
        updateParticipant(participant);
        protocolService.modifiedEntity(participant);
    }

    private Event updateParticipants(Event event, List<Participant> participants) {
        participantRepository.removeFromEvent(event.id());
        return insertParticipants(event, participants);
    }

    private Event insertParticipants(Event event, List<Participant> participants) {
        return new Event(event, participants.stream()
                .map(p -> insertParticipant(event.id(), p.user_id(), p.participate()))
                .toList());
    }

    private Participant insertParticipant(long eventId, long userId, boolean participate) {
        participantRepository.insert(eventId, userId, participate);
        return new Participant(eventId, userId, participate);
    }

    private void updateParticipant(Participant participant) {
        participantRepository.update(
                participant.event_id(),
                participant.user_id(),
                participant.participate());
    }

    private Event addParticipants(Event event) {
        return new Event(event, participantRepository.findByEventId(event.id()));
    }

    private Event assertEventVersion(Event event) {
        eventRepository.findByIdAndVersion(event.id(), event.version()).orElseThrow(() -> new OptimisticLockingFailureException("cannot find event %s with id %d in version %d".formatted(event.name(), event.id(), event.version())));
        return event;
    }

    private Event assertNew(Event event) {
        if (event.id() != null && event.id() > 0) {
            throw new DuplicateKeyException(
                    "Event %s with id %d already exists"
                            .formatted(event.name(), event.id()));
        }
        return event;
    }
}
