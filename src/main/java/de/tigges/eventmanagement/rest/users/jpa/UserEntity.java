package de.tigges.eventmanagement.rest.users.jpa;

import java.util.HashSet;
import java.util.Set;

import de.tigges.eventmanagement.rest.events.jpa.ParticipantEntity;
import de.tigges.eventmanagement.rest.users.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String vorname;
    private String nachname;
    private UserStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id")
    private InstrumentEntity instrument;

    @OneToMany(mappedBy = "user")
    private Set<ParticipantEntity> participants = new HashSet<>();

    public UserEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return id;
    }

    public UserEntity setVorname(String vorname) {
        this.vorname = vorname;
        return this;
    }

    public String getVorname() {
        return vorname;
    }

    public UserEntity setNachname(String nachname) {
        this.nachname = nachname;
        return this;
    }

    public String getNachname() {
        return nachname;
    }

    public UserEntity setStatus(UserStatus status) {
        this.status = status;
        return this;
    }

    public UserStatus getStatus() {
        return status;
    }

    public UserEntity setInstrument(InstrumentEntity instrument) {
        this.instrument = instrument;
        return this;
    }

    public InstrumentEntity getInstrument() {
        return instrument;
    }

    public UserEntity addParticipant(ParticipantEntity participantEntity) {
        participants.add(participantEntity);
        participantEntity.setUser(this);
        return this;
    }
    public Set<ParticipantEntity> getParticipants() { return participants;}

    @Override
    public int hashCode() {
        return 536356;
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
        UserEntity other = (UserEntity) obj;
        if (id == null) {
            return false;
        } else
            return id == other.id;
    }

    @Override
    public String toString() {
        return "UserEntity[id=" + id
                + ", name=" + vorname + " " + nachname
                + ", status=" + status
                + "]";
    }
}
