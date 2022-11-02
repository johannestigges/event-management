package de.tigges.eventmanagement.rest.protocol.jpa;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import de.tigges.eventmanagement.rest.protocol.ProtocolType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ProtocolEntity {
    @Id
    @GeneratedValue
    private Long id;

    private ProtocolType type;
    @CreatedDate
    private LocalDateTime createdAt;

    private String entityType;
    private Long entityId;

    @Column(length = 4000)
    private String data;
}
