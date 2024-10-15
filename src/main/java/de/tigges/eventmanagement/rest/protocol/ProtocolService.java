package de.tigges.eventmanagement.rest.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProtocolService {
    private final ProtocolRepository repository;

    public <T extends Protocollable> T newEntity(T data) {
        protocol(data.id(), data.protocolName(), data, ProtocolType.CREATE);
        return data;
    }

    public <T extends Protocollable> T modifiedEntity(T data) {
        protocol(data.id(), data.protocolName(), data, ProtocolType.UPDATE);
        return data;
    }

    public <T extends Protocollable> T deletedEntity(T data) {
        protocol(data.id(), data.protocolName(), data, ProtocolType.DELETE);
        return data;
    }

    public void deletedEntity(Long entityId, String entityType) {
        protocol(entityId, entityType, null, ProtocolType.DELETE);
    }

    private void protocol(Long entityId, String entityType, Object data, ProtocolType type) {
        repository.save(createProtocol(entityId, entityType, data, type));
    }

    private Protocol createProtocol(Long entityId, String entityType, Object data, ProtocolType type) {
        return Protocol.builder()
                .entityId(entityId)
                .entityType(entityType)
                .createdAt(LocalDateTime.now())
                .type(type)
                .data(deserialize(data))
                .build();
    }

    private String deserialize(Object data) {
        if (data == null) {
            return null;
        }
        try {
            return Jackson2ObjectMapperBuilder.json().build().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
