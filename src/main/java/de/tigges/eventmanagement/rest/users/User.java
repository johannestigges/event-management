package de.tigges.eventmanagement.rest.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    Long id;
    String vorname;
    String nachname;
    UserStatus status;
    Instrument instrument;
}
