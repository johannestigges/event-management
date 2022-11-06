package de.tigges.eventmanagement.rest.protocol.jpa;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import de.tigges.eventmanagement.rest.protocol.ProtocolType;
import lombok.Data;

@Entity
@Data
public class ProtocolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private ProtocolType type;
    @CreatedDate
    private LocalDateTime createdAt;

    private String entityType;
    private Long entityId;

    @Column(length = 4000)
    private String data;
}
