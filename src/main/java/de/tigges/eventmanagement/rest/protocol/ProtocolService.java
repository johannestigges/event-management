package de.tigges.eventmanagement.rest.protocol;

import java.time.LocalDateTime;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.tigges.eventmanagement.rest.protocol.jpa.ProtocolEntity;
import de.tigges.eventmanagement.rest.protocol.jpa.ProtocolRepository;
import lombok.RequiredArgsConstructor;

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
        var entity = new ProtocolEntity();
        entity.setEntityId(entityId);
        entity.setEntityType(entityType);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setType(type);
        entity.setData(deserialize(data));
        repository.save(entity);
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
