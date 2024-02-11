package de.tigges.eventmanagement.rest.users;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<User> selectAllWithInstruments() {
        return jdbcTemplate.query(
                "SELECT * FROM ev_user u LEFT JOIN ev_instrument i ON i.id = u.instrument_id ORDER BY u.instrument_id",
                new UserMapper(true));
    }

    public Optional<User> findUserWithInstrument(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * FROM ev_user u LEFT JOIN ev_instrument i ON i.id = u.instrument_id WHERE u.id = ?",
                new UserMapper(true), id));
    }

    public User insert(User user) {
        var insert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("ev_user")
                .usingGeneratedKeyColumns("id");
        var parameters = new HashMap<String, Object>();
        parameters.put("version", 0);
        parameters.put("vorname", user.vorname());
        parameters.put("nachname", user.nachname());
        parameters.put("status", user.status());
        if (user.instrument() != null) {
            parameters.put("instrument_id", user.instrument().id());
        }
        var id = insert.executeAndReturnKey(parameters);
        return findUserWithInstrument(id.longValue()).orElseThrow(RuntimeException::new);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM ev_user WHERE id = ?", id);
    }

    User update(User user) {
        if (jdbcTemplate.update(
                "UPDATE ev_user SET vorname = ?, nachname = ?, status = ?, instrument_id = ?, version = ? WHERE id = ? AND version = ?",
                user.vorname(),
                user.nachname(),
                user.status().toString(),
                user.instrument().id(),
                user.version() + 1,
                user.id(),
                user.version()) != 1) {
            throw new OptimisticLockingFailureException("Fehler beim Aktualisieren von user " + user);
        }
        return findUserWithInstrument(user.id()).orElseThrow(RuntimeException::new);
    }

    @RequiredArgsConstructor
    public static class UserMapper implements RowMapper<User> {

        private final boolean withInstrument;

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getLong("id"),
                    rs.getLong("version"),
                    rs.getString("vorname"),
                    rs.getString("nachname"),
                    UserStatus.valueOf(rs.getString("status")),
                    withInstrument ? mapInstrument(rs) : null);
        }

        private Instrument mapInstrument(ResultSet rs) throws SQLException {
            return new Instrument(
                    rs.getLong("instrument_id"),
                    rs.getString("instrument"),
                    rs.getString("gruppe"));
        }
    }
}
