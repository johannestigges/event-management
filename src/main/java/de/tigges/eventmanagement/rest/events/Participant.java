package de.tigges.eventmanagement.rest.events;

import de.tigges.eventmanagement.rest.protocol.Protocollable;
import org.springframework.data.relational.core.mapping.Table;

@Table("ev_participant")
public record Participant(Long event_id, Long user_id, Boolean participate) implements Protocollable {
    @Override
    public Long id() {
        return event_id * 100000 + user_id;
    }

    @Override
    public String protocolName() {
        return "Participant";
    }
}
