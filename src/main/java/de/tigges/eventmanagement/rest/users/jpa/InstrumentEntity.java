package de.tigges.eventmanagement.rest.users.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "instrument")
@Data
public class InstrumentEntity {
    @Id
    private Long id;
    private String instrument;
    private String gruppe;
}
