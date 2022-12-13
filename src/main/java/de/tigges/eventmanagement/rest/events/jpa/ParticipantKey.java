package de.tigges.eventmanagement.rest.events.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ParticipantKey implements Serializable {

    @Column(name = "event_id")
    Long eventId;
    
    @Column(name = "user_id")
    Long userId;
}