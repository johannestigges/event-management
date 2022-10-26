package de.tigges.eventmanagement.rest.users.jpa;

import de.tigges.eventmanagement.rest.users.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String vorname;
    private String nachname;
    private UserType typ;
}
