package de.tigges.eventmanagement.rest.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Participant {
    private Long event_id;
    private Long user_id;

    private Boolean participate;
}
