package de.tigges.eventmanagement.rest.users.jpa;

import de.tigges.eventmanagement.rest.users.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String vorname;
    private String nachname;
    private UserStatus status;
}
