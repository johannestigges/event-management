package de.tigges.eventmanagement.rest.users;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("EV_INSTRUMENT")
public class Instrument {

    private Long id;
    private String instrument;
    private String gruppe;
}
