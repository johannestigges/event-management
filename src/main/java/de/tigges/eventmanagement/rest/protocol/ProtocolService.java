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

    public void newEntity(Long entityId, String entityType, Object data) {
        protocol(entityId, entityType, data, ProtocolType.CREATE);
    }

    public void modifiedEntity(Long entityId, String entityType, Object data) {
        protocol(entityId, entityType, data, ProtocolType.UPDATE);
    }

    public void deletedEntity(Long entityId, String entityType) {
        protocol(entityId, entityType, null, ProtocolType.DELETE);
    }

    private void protocol(Long entityId, String entityType, Object data, ProtocolType type) {
        repository.save(Protocol.builder()
                .entityId(entityId)
                .entityType(entityType)
                .createdAt(LocalDateTime.now())
                .type(type)
                .data(deserialize(data))
                .build());
    }

    private String deserialize(Object data) {
        try {
            if (data == null)
                return null;
            return Jackson2ObjectMapperBuilder.json().build().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
