package de.tigges.eventmanagement.rest.events;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {
    private Long id;
    private Long version;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Participant> participants;
}
