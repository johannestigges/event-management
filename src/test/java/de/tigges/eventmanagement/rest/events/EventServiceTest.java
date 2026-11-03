package de.tigges.eventmanagement.rest.events;

import de.tigges.eventmanagement.rest.protocol.ProtocolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class EventServiceTest {
    private final EventRepository eventRepository = mock(EventRepository.class);
    private final ParticipantRepository participantRepository = mock(ParticipantRepository.class);
    private final ProtocolService protocolService = mock(ProtocolService.class);

    private EventService eventService;

    @BeforeEach
    void init() {
        eventService = new EventService(eventRepository, participantRepository, protocolService);
        when(protocolService.newEntity(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(protocolService.modifiedEntity(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
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

    @Test
    void getOneUnknownEvent() {
        when(eventRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getOne(9L))
                .isInstanceOf(EmptyResultDataAccessException.class);

        verifyNoInteractions(participantRepository);
    }

    @Test
    void insert() {
        var participants = toParticipants(1L, 1L, 2L);
        var event = new Event(null, null, "event3", LocalDateTime.now(), LocalDateTime.now(), participants);
        when(eventRepository.save(event)).thenReturn(createEvent(3L));

        var inserted = eventService.insert(event);

        assertEvent(inserted, 3L, 1L, 2L);
        verify(participantRepository).insert(3L, 1L, true);
        verify(participantRepository).insert(3L, 2L, true);
        verify(protocolService).newEntity(inserted);
    }

    @Test
    void insertExistingEvent() {
        var event = createEvent(1L);

        assertThatThrownBy(() -> eventService.insert(event))
                .isInstanceOf(DuplicateKeyException.class);

        verify(eventRepository, never()).save(any(Event.class));
        verifyNoInteractions(participantRepository, protocolService);
    }

    @Test
    void update() {
        var participants = List.of(
                new Participant(1L, 1L, true),
                new Participant(1L, 2L, false));
        var event = new Event(1L, 1L, "event1", LocalDateTime.now(), LocalDateTime.now(), participants);
        when(eventRepository.findByIdAndVersion(1L, 1L)).thenReturn(Optional.of(createEvent(1L)));
        when(eventRepository.save(event)).thenReturn(event);

        var updated = eventService.update(event);

        assertThat(updated.id()).isEqualTo(1L);
        assertThat(updated.version()).isEqualTo(1L);
        assertThat(updated.participants()).containsExactly(
                new Participant(1L, 1L, true),
                new Participant(1L, 2L, false));
        verify(participantRepository).removeFromEvent(1L);
        verify(participantRepository).insert(1L, 1L, true);
        verify(participantRepository).insert(1L, 2L, false);
        verify(protocolService).modifiedEntity(updated);
    }

    @Test
    void updateUnknownVersion() {
        var event = new Event(1L, 2L, "event1", LocalDateTime.now(), LocalDateTime.now(), List.of());
        when(eventRepository.findByIdAndVersion(1L, 2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.update(event))
                .isInstanceOf(OptimisticLockingFailureException.class);

        verify(eventRepository, never()).save(any(Event.class));
        verifyNoInteractions(participantRepository, protocolService);
    }

    @Test
    void delete() {
        var event = createEvent(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        eventService.delete(1L);

        verify(participantRepository).removeFromEvent(1L);
        verify(eventRepository).deleteById(1L);
        verify(protocolService).deletedEntity(event);
    }

    @Test
    void deleteUnknownEvent() {
        when(eventRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.delete(9L))
                .isInstanceOf(EmptyResultDataAccessException.class);

        verify(eventRepository, never()).deleteById(anyLong());
        verifyNoInteractions(participantRepository, protocolService);
    }

    @Test
    void updateParticipant() {
        var participant = new Participant(1L, 2L, false);

        eventService.updateParticipant(participant, 1L);

        verify(participantRepository).update(1L, 2L, false);
        verify(protocolService).modifiedEntity(participant);
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
