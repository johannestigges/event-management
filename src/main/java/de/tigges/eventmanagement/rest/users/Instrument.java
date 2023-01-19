package de.tigges.eventmanagement.rest.users;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Instrument {

    private Long id;
    private String instrument;
    private String gruppe;
}
