package de.tigges.eventmanagement.rest.protocol;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import de.tigges.eventmanagement.rest.protocol.jpa.ProtocolEntity;

public class ProtocolMapper {

    public static Set<Protocol> map(Iterable<ProtocolEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(p -> map(p)).collect(Collectors.toSet());
    }

    private static Protocol map(ProtocolEntity entity) {
        var p = new Protocol();
        p.setCreatedAt(entity.getCreatedAt());
        p.setType(entity.getType());
        p.setEntityType(entity.getEntityType());
        p.setEntityId(entity.getEntityId());
        p.setData(entity.getData());
        return p;
    }
}
