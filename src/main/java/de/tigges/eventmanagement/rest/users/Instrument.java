package de.tigges.eventmanagement.rest.users;

import de.tigges.eventmanagement.rest.protocol.Protocollable;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "EV_INSTRUMENT")
public record Instrument(Long id, String instrument, String gruppe) implements Protocollable {
    @Override
    public String protocolName() {
        return "Instrument";
    }
}
