package de.tigges.eventmanagement.rest.events.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.tigges.eventmanagement.rest.users.jpa.UserEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class ParticipantEntity {

    @EmbeddedId
    ParticipantKey id;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    @JsonIgnore // important to avoid endless loop in prococolservice!!
    private EventEntity event;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnore // important to avoid endless loop in prococolservice!!
    private UserEntity user;

    private Boolean participate;

    public ParticipantEntity setId(ParticipantKey id) {
        this.id = id;
        return this;
    }

    public ParticipantEntity setId(Long event_id, Long user_id) {
        var key = new ParticipantKey();
        key.setEventId(event_id);
        key.setUserId(user_id);
        return setId(key);
    }

    public ParticipantKey getId() {
        return id;
    }

    public ParticipantEntity setParticipate(boolean participate) {
        this.participate = participate;
        return this;
    }

    public boolean isParticipate() {
        return participate;
    }

    public ParticipantEntity setEvent(EventEntity event) {
        this.event = event;
        return this;
    }

    public EventEntity getEvent() {
        return event;
    }

    public ParticipantEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        return 692843;
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
        ParticipantEntity other = (ParticipantEntity) obj;
        if (id == null) {
            return false;
        } else
            return id == other.id;
    }

    @Override
    public String toString() {
        return "ParticipantEntity[event_id=" + id.getEventId()
                + ", user_id=" + id.getUserId()
                + ", participate=" + participate + "]";
    }
}
