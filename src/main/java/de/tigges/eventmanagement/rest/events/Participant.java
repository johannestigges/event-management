package de.tigges.eventmanagement.rest.events;

public record Participant(Long event_id, Long user_id, Boolean participate) {
}
