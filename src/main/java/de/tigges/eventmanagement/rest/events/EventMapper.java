package de.tigges.eventmanagement.rest.events;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import de.tigges.eventmanagement.rest.events.jpa.EventEntity;
import de.tigges.eventmanagement.rest.events.jpa.ParticipantEntity;

public class EventMapper {
    public static Set<Event> mapEntities(Iterable<EventEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(e -> mapEntity(e))
                .collect(Collectors.toSet());
    }

    public static Event mapEntity(EventEntity entity) {
        var event = new Event();
        event.setId(entity.getId());
        event.setName(entity.getName());
        event.setStart(entity.getStart_event());
        event.setEnd(entity.getEnd_event());
        entity.getParticipants().forEach(p -> event.getParticipants().add(map(p)));
        return event;
    }

    public static EventEntity map(Event event) {
        var entity = new EventEntity();
        entity.setId(event.getId());
        entity.setName(event.getName());
        entity.setStart_event(event.getStart());
        entity.setEnd_event(event.getEnd());
        event.getParticipants().forEach(p -> entity.getParticipants().add(map(p)));
        return entity;
    }

    public static Participant map(ParticipantEntity e) {
        var p = new Participant();
        p.setId(e.getId());
        p.setUser_id(e.getUser_id());
        p.setParticipate(e.getParticipate());
        return p;
    }

    public static ParticipantEntity map(Participant p) {
        var e = new ParticipantEntity();
        e.setId(p.getId());
        e.setUser_id(p.getUser_id());
        e.setParticipate(p.getParticipate());
        return e;
    }
}
