package de.tigges.eventmanagement.rest.users;

import java.util.List;
import java.util.stream.Collectors;

import de.tigges.eventmanagement.rest.users.jpa.InstrumentEntity;
import de.tigges.eventmanagement.rest.users.jpa.UserEntity;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserMapper {

    public static List<User> mapEntities(List<UserEntity> entities) {
        return entities.stream()
                .map(e -> mapEntity(e))
                .collect(Collectors.toList());
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

    public static List<Instrument> mapInstruments(List<InstrumentEntity> entities) {
        log.warn("Instruments: {}",
                entities.stream().map(s -> Long.toString(s.getId())).collect(Collectors.joining(",")));
        return entities.stream()
                .map(i -> mapInstrument(i))
                .collect(Collectors.toList());
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
        var instrumentEntity = new InstrumentEntity();
        instrumentEntity.setId(instrument.getId());
        instrumentEntity.setInstrument(instrument.getInstrument());
        instrumentEntity.setGruppe(instrument.getGruppe());
        return instrumentEntity;
    }
}