package de.tigges.eventmanagement.rest.users;

import lombok.Data;

@Data
public class User {
    Long id;
    String vorname;
    String nachname;
    UserStatus status;
    Instrument instrument;
}
