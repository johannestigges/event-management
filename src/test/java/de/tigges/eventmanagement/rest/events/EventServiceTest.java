package de.tigges.eventmanagement.rest.events;

import de.tigges.eventmanagement.rest.protocol.ProtocolService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EventServiceTest {
    private final EventRepository eventRepository = mock(EventRepository.class);
    private final ParticipantRepository participantRepository = mock(ParticipantRepository.class);
    private final ProtocolService protocolService = mock(ProtocolService.class);

    @Test void getAll() {
        when (eventRepository.findAll()).thenReturn(List.of(
                new Event(1L, 1L, "name1", LocalDateTime.now(), LocalDateTime.now(),List.of())
        ));
    }
}