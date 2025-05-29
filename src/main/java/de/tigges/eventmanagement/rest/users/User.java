package de.tigges.eventmanagement.rest.users;

import de.tigges.eventmanagement.rest.protocol.Protocollable;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "ev_user")
public record User(
        Long id,
        Long version,
        String vorname,
        String nachname,
        UserStatus status,
        Instrument instrument) implements Protocollable {
    @Override
    public String protocolName() {
        return "User";
    }
}
