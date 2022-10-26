package de.tigges.eventmanagement.rest.events;

import lombok.Data;

@Data
public class Participant {
    private Long id;
    private Long user_id;
    private Boolean participate;
}
