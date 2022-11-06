package de.tigges.eventmanagement.rest.users;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import de.tigges.eventmanagement.rest.users.jpa.UserEntity;

public class UserMapper {

    public static Set<User> mapEntities(Iterable<UserEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(e -> mapEntity(e))
                .collect(Collectors.toSet());
    }

    public static User mapEntity(UserEntity entity) {
        var user = new User();
        user.setId(entity.getId());
        user.setVorname(entity.getVorname());
        user.setNachname(entity.getNachname());
        user.setStatus(entity.getStatus());
        return user;
    }

    public static UserEntity map(User user) {
        var entity = new UserEntity();
        entity.setId(user.getId());
        entity.setVorname(user.getVorname());
        entity.setNachname(user.getNachname());
        entity.setStatus(user.getStatus());
        return entity;
    }
}