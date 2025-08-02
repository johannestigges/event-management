package de.tigges.eventmanagement.rest.events;

import de.tigges.eventmanagement.rest.protocol.ProtocolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EventServiceTest {
    private final EventRepository eventRepository = mock(EventRepository.class);
    private final ParticipantRepository participantRepository = mock(ParticipantRepository.class);
    private final ProtocolService protocolService = mock(ProtocolService.class);

    private EventService eventService;

    @BeforeEach
    void init() {
        eventService = new EventService(eventRepository, participantRepository, protocolService);
    }

    @Test
    void getAll() {
        when(eventRepository.findAllByOrderByStartAtAsc())
                .thenReturn(toEvents(1L, 2L));
        initParticipants(1L, 1L, 2L);
        initParticipants(2L, 1L, 2L);

        var events = eventService.getAll();

        assertThat(events).hasSize(2);
        assertEvent(events.get(0), 1L, 1L, 2L);
        assertEvent(events.get(1), 2L, 1L, 2L);
    }

    @Test
    void getOne() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(createEvent(1L)));
        initParticipants(1L, 10L, 11L);

        var event = eventService.getOne(1L);

        assertEvent(event, 1L, 10L, 11L);
    }

    private void initParticipants(long eventId, long... userIds) {
        when(participantRepository.findByEventId(eventId))
                .thenReturn(toParticipants(eventId, userIds));
    }

    private List<Event> toEvents(long... eventIds) {
        return LongStream.of(eventIds).mapToObj(this::createEvent).toList();
    }

    private Event createEvent(long eventId) {
        return new Event(eventId, 1L, "event" + eventId, LocalDateTime.now(), LocalDateTime.now(), List.of());
    }

    private void assertEvent(Event event, long eventId, long... userIds) {
        assertThat(event.id()).isEqualTo(eventId);
        assertThat(event.version()).isEqualTo(1L);
        assertThat(event.name()).isEqualTo("event" + eventId);
        assertParticipants(event, userIds);
    }

    private void assertParticipants(Event event, long... userIds) {
        assertThat(event.participants()).hasSize(userIds.length);
        for (int i = 0; i < userIds.length; i++) {
            assertParticipant(event.participants().get(i), event.id(), userIds[i]);
        }
    }

    private void assertParticipant(Participant participant, long eventId, long userId) {
        assertThat(participant.event_id()).isEqualTo(eventId);
        assertThat(participant.user_id()).isEqualTo(userId);
        assertThat(participant.participate()).isTrue();
    }

    private List<Participant> toParticipants(long eventId, long... userIds) {
        return LongStream.of(userIds).mapToObj(userId -> new Participant(eventId, userId, true)).toList();
    }
}
