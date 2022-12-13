package de.tigges.eventmanagement.rest.events.jpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class EventEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipantEntity> participants = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public EventEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public EventEntity setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public EventEntity setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
        return this;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public EventEntity setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
        return this;
    }

    public List<ParticipantEntity> getParticipants() {
        return participants;
    }

    public EventEntity addParticipant(ParticipantEntity participant) {
        getParticipants().add(participant);
        participant.setEvent(this);
        return this;
    }

    @Override
    public int hashCode() {
        return 14324;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EventEntity other = (EventEntity) obj;
        if (id == null) {
            return false;
        } else
            return id == other.id;
    }

    @Override
    public String toString() {
        return "EventEntity[id=" + id + ", name=" + name + "]";
    }
}
