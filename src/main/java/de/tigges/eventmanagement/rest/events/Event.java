package de.tigges.eventmanagement.rest.events;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Event {
    private Long id;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Participant> participants = new ArrayList<>();
}
