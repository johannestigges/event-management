package de.tigges.eventmanagement.rest.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.tigges.eventmanagement.rest.protocol.Protocollable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "ev_user")
public record User(
        @Id Long id,
        @Version Long version,
        String vorname,
        String nachname,
        UserStatus status,
        @JsonIgnore @Column("instrument_id") Long instrumentId,
        @Transient Instrument instrument) implements Protocollable {

    @PersistenceCreator
    public User(Long id, Long version, String vorname, String nachname, UserStatus status, Long instrumentId) {
        this(id, version, vorname, nachname, status, instrumentId, null);
    }

    public User withInstrument(Instrument instrument) {
        return new User(id, version, vorname, nachname, status,
                instrument == null ? instrumentId : instrument.id(),
                instrument);
    }

    public User withInstrumentId(Long instrumentId) {
        return new User(id, version, vorname, nachname, status, instrumentId, instrument);
    }

    public User asNew() {
        return new User(null, null, vorname, nachname, status, instrumentId, instrument);
    }

    @Override
    public String protocolName() {
        return "User";
    }
}
