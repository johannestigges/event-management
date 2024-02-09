package de.tigges.eventmanagement.rest.events;

import java.time.LocalDateTime;
import java.util.List;

public record Event(Long id,
                    Long version,
                    String name,
                    LocalDateTime start,
                    LocalDateTime end,
                    List<Participant> participants) {
    public Event(Event source, List<Participant> participants) {
        this(source.id(), source.version(), source.name(), source.start(), source.end(), participants);
    }
}
