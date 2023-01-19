package de.tigges.eventmanagement.rest.protocol;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@Table("ev_protocol")
public class Protocol {

    @Id
    Long id;
    private LocalDateTime createdAt;
    private ProtocolType type;

    private String entityType;
    private Long entityId;
    private String data;
}
