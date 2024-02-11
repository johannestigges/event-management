package de.tigges.eventmanagement.rest.users;

public record User(
        Long id,
        Long version,
        String vorname,
        String nachname,
        UserStatus status,
        Instrument instrument) {
}
