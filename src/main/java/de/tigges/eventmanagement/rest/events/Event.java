package de.tigges.eventmanagement.rest.events;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Event {
    private Long id;
    private Long version;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    @Builder.Default
    private List<Participant> participants = new ArrayList<>();
}
