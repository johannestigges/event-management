package de.tigges.eventmanagement.rest.events;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import de.tigges.eventmanagement.rest.events.jpa.EventEntity;
import de.tigges.eventmanagement.rest.events.jpa.ParticipantEntity;
import de.tigges.eventmanagement.rest.users.jpa.UserEntity;

public class EventMapper {

    public static Set<Event> mapEntities(Iterable<EventEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(e -> mapEntity(e, true))
                .collect(Collectors.toSet());
    }

    public static Event mapEntity(EventEntity entity, boolean withParticipants) {
        if (entity == null) {
            return null;
        }
        var event = Event.builder()
                .id(entity.getId())
                .name(entity.getName())
                .start(entity.getStartAt())
                .end(entity.getEndAt()).build();
        if (withParticipants) {
            entity.getParticipants().forEach(p -> event.getParticipants().add(map(p)));
        }
        return event;
    }

    public static EventEntity map(Event event) {
        var entity = new EventEntity()
                .setId(event.getId())
                .setName(event.getName())
                .setStartAt(event.getStart())
                .setEndAt(event.getEnd());
        event.getParticipants().forEach(p -> entity.addParticipant(map(p)));
        return entity;
    }

    public static Participant map(ParticipantEntity e) {
        return e == null
                ? null
                : Participant.builder()
                        .event_id(e.getId().getEventId())
                        .user_id(e.getId().getUserId())
                        .participate(e.isParticipate())
                        .build();
    }

    public static ParticipantEntity map(Participant p) {
        return new ParticipantEntity()
                .setId(p.getEvent_id(), p.getUser_id())
                .setParticipate(p.getParticipate())
                .setEvent(new EventEntity().setId(p.getEvent_id()))
                .setUser(new UserEntity().setId(p.getUser_id()));
    }
}
