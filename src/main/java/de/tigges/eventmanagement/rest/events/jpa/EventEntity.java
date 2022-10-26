package de.tigges.eventmanagement.rest.events.jpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class EventEntity {
    @Id
    private Long id;
    private String name;
    private LocalDateTime start_event;
    private LocalDateTime end_event;

    @OneToMany(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<ParticipantEntity> participants = new ArrayList<>();
}
