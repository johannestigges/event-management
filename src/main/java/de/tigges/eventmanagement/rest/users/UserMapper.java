package de.tigges.eventmanagement.rest.users;

import java.util.List;
import java.util.stream.Collectors;

import de.tigges.eventmanagement.rest.users.jpa.InstrumentEntity;
import de.tigges.eventmanagement.rest.users.jpa.UserEntity;

public class UserMapper {
  public static List<User> mapEntities(List<UserEntity> entities) {
    return entities.stream()
        .map(UserMapper::mapEntity)
        .collect(Collectors.toList());
  }

  public static User mapEntity(UserEntity entity) {
    return entity == null
        ? null
        : User.builder()
            .id(entity.getId())
            .vorname(entity.getVorname())
            .nachname(entity.getNachname())
            .status(entity.getStatus())
            .instrument(mapInstrument(entity.getInstrument()))
            .build();
  }

  public static UserEntity map(User user) {
    return user == null
        ? null
        : new UserEntity()
            .setId(user.getId())
            .setVorname(user.getVorname())
            .setNachname(user.getNachname())
            .setStatus(user.getStatus())
            .setInstrument(mapInstrument(user.getInstrument()));
  }

  public static List<Instrument> mapInstruments(List<InstrumentEntity> entities) {
    return entities.stream()
        .map(i -> mapInstrument(i))
        .collect(Collectors.toList());
  }

  private static Instrument mapInstrument(InstrumentEntity entity) {
    return entity == null
        ? null
        : Instrument.builder()
            .id(entity.getId())
            .instrument(entity.getInstrument())
            .gruppe(entity.getGruppe())
            .build();
  }

  private static InstrumentEntity mapInstrument(Instrument instrument) {
    return instrument == null
        ? null
        : new InstrumentEntity()
            .setId(instrument.getId())
            .setInstrument(instrument.getInstrument())
            .setGruppe(instrument.getGruppe());
  }
}