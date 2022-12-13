package de.tigges.eventmanagement.rest.users.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "instrument")
public class InstrumentEntity {
    @Id
    private Long id;
    private String instrument;
    private String gruppe;

    public Long getId() {
        return id;
    }

    public InstrumentEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getInstrument() {
        return instrument;
    }

    public InstrumentEntity setInstrument(String instrument) {
        this.instrument = instrument;
        return this;
    }

    public String getGruppe() {
        return gruppe;
    }

    public InstrumentEntity setGruppe(String gruppe) {
        this.gruppe = gruppe;
        return this;
    }

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
        InstrumentEntity other = (InstrumentEntity) obj;
        if (id == null) {
            return false;
        } else
            return id == other.id;
    }

    @Override
    public String toString() {
        return "InstrumentEntity[" + instrument
                + ", gruppe=" + gruppe
                + ", id=" + id
                + "]";
    }
}
