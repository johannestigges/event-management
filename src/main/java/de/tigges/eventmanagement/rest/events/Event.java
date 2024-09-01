package de.tigges.eventmanagement.rest.events;

import de.tigges.eventmanagement.rest.protocol.Protocollable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Table(name = "EV_EVENT")
public record Event(@Id Long id,
                    @Version Long version,
                    String name,
                    LocalDateTime startAt,
                    LocalDateTime endAt,
                    @Transient List<Participant> participants) implements Protocollable {

    public Event(Long id, Long version, String name, LocalDateTime startAt, LocalDateTime endAt) {
        this(id, version, name, startAt, endAt, Collections.emptyList());
    }

    public Event(Event source, List<Participant> participants) {
        this(source.id(),
                source.version(),
                source.name(),
                source.startAt(),
                source.endAt(),
                participants);
    }

    @Override
    public String protocolName() {
        return "Event";
    }
}
