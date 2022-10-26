package de.tigges.eventmanagement.rest.events.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ParticipantEntity {
    @Id
    @GeneratedValue
    Long id;
    Long user_id;
    Boolean participate;

}
