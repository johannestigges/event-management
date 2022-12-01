package de.tigges.eventmanagement.rest.users.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u LEFT JOIN u.instrument i ORDER BY u.instrument.id")
    List<UserEntity> selectAllWithInstruments();

    @Query("SELECT u FROM UserEntity u LEFT JOIN u.instrument i WHERE u.id = ?1")
    Optional<UserEntity> findOneWithInstrument(Long id);

}
