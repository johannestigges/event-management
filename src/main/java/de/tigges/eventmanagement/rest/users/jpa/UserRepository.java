package de.tigges.eventmanagement.rest.users.jpa;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query("select u from UserEntity u LEFT JOIN u.instrument i")
    Iterable<UserEntity> selectAllWithInstruments(Sort sort);

    @Query("select u from UserEntity u LEFT JOIN u.instrument i WHERE u.id = ?1 ")
    Optional<UserEntity> findOneWithInstrument(Long id);

}
