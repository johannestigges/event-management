package de.tigges.eventmanagement.rest.users;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import de.tigges.eventmanagement.rest.users.jpa.InstrumentEntity;
import de.tigges.eventmanagement.rest.users.jpa.UserEntity;

public class UserMapper {

    private static InstrumentEntity instrumentEntity;

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
        user.setInstrument(mapInstrument(entity.getInstrument()));
        return user;
    }

    public static UserEntity map(User user) {
        var entity = new UserEntity();
        entity.setId(user.getId());
        entity.setVorname(user.getVorname());
        entity.setNachname(user.getNachname());
        entity.setStatus(user.getStatus());
        entity.setInstrument(mapInstrument(user.getInstrument()));
        return entity;
    }

    public static Set<Instrument> mapInstruments(Iterable<InstrumentEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(i -> mapInstrument(i))
                .collect(Collectors.toSet());
    }

    private static Instrument mapInstrument(InstrumentEntity entity) {
        if (entity == null) {
            return null;
        }
        return Instrument.builder()
                .id(entity.getId())
                .instrument(entity.getInstrument())
                .gruppe(entity.getGruppe())
                .build();
    }

    private static InstrumentEntity mapInstrument(Instrument instrument) {
        if (instrument == null) {
            return null;
        }
        instrumentEntity = new InstrumentEntity();
        instrumentEntity.setId(instrument.getId());
        instrumentEntity.setInstrument(instrument.getInstrument());
        instrumentEntity.setGruppe(instrument.getGruppe());
        return instrumentEntity;
    }
}