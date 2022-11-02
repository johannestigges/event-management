package de.tigges.eventmanagement.rest.protocol;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Protocol {

    private LocalDateTime createdAt;
    private ProtocolType type;

    private String entityType;
    private Long entityId;
    private String data;
}
