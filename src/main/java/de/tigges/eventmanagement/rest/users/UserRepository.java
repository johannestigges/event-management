package de.tigges.eventmanagement.rest.users;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAllByOrderByInstrumentIdAsc();

    default User insert(User user) {
        if (user.id() != null || user.version() != null) {
            throw new IllegalArgumentException("insert requires a new user without id and version");
        }
        return save(user);
    }

    default User update(User user) {
        if (user.id() == null || user.version() == null) {
            throw new IllegalArgumentException("update requires a user with id and version");
        }
        return save(user);
    }
}
