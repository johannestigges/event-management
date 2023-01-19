package de.tigges.eventmanagement.rest.users;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("EV_USER")
public class User {
    @Id
    Long id;

    @Version
    Long version;

    String vorname;
    String nachname;
    UserStatus status;
    Instrument instrument;
}
