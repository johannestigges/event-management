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
                "SELECT * FROM ev_user u LEFT JOIN ev_instrument i on i.id = u.instrument_id ORDER BY u.instrument_id",
                new UserMapper(true));
    }

    public Optional<User> findUserWithInstrument(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * FROM ev_user u LEFT JOIN ev_instrument i on i.id = u.instrument_id WHERE u.id = ?",
                new UserMapper(true), id));
    }

    public User insert(User user) {
        var insert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("ev_user")
                .usingGeneratedKeyColumns("id");
        var parameters = new HashMap<String, Object>();
        parameters.put("version", 0);
        parameters.put("vorname", user.getVorname());
        parameters.put("nachname", user.getNachname());
        parameters.put("status", user.getStatus());
        if (user.getInstrument() != null) {
            parameters.put("instrument_id", user.getInstrument().getId());
        }
        Number id = insert.executeAndReturnKey(parameters);
        return findUserWithInstrument(id.longValue()).orElseThrow(RuntimeException::new);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM ev_user WHERE id = ?", id);
    }

    User update(User user) {
        if (jdbcTemplate.update("UPDATE ev_user SET vorname =? , nachname = ?, status = ?, version = ? WHERE id = ? AND version = ?",
                user.getVorname(), user.getNachname(), user.getStatus(), user.getVersion() + 1,
                user.getId(), user.getVersion()) != 1) {
            throw new OptimisticLockingFailureException("Fehler beim Aktualisieren von user " + user);
        }
        return findUserWithInstrument(user.getId()).orElseThrow(RuntimeException::new);
    }

    @RequiredArgsConstructor
    public static class UserMapper implements RowMapper<User> {

        private final boolean withInstrument;

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            var user = User.builder()
                    .id(rs.getLong("id"))
                    .version(rs.getLong("version"))
                    .vorname(rs.getString("vorname"))
                    .nachname(rs.getString("nachname"))
                    .status(UserStatus.valueOf(rs.getString("status")))
                    .build();
            if (withInstrument) {
                user.setInstrument(Instrument.builder()
                        .id(rs.getLong("instrument_id"))
                        .instrument(rs.getString("instrument"))
                        .gruppe(rs.getString("gruppe"))
                        .build());
            }
            return user;
        }
    }
}
