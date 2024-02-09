package de.tigges.eventmanagement.rest.events;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;

    List<Event> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM ev_event ORDER BY start_at asc ",
                new EventMapper());
    }

    Optional<Event> findByIdAndVersion(Long id, Long version) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * FROM ev_event WHERE id = ? AND version = ? ",
                new EventMapper(), id, version));
    }

    Optional<Event> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * FROM ev_event WHERE id = ?",
                new EventMapper(), id));
    }

    public Event insert(Event event) {
        var insert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("ev_event")
                .usingGeneratedKeyColumns("id");
        var id = insert.executeAndReturnKey(Map.of(
                "version", 0,
                "name", event.name(),
                "start_at", event.start(),
                "end_at", event.end()));
        return findById(id.longValue()).orElseThrow(RuntimeException::new);
    }

    Event update(Event event) {
        if (jdbcTemplate.update(
                "UPDATE ev_event SET name =?, start_at = ?, end_at = ?, version = ? WHERE id = ? AND version = ?",
                event.name(),
                event.start(),
                event.end(),
                event.version() + 1,
                event.id(),
                event.version()) != 1) {
            throw new OptimisticLockingFailureException("Fehler beim Aktualisieren von event " + event);
        }
        return findById(event.id()).orElseThrow(RuntimeException::new);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(
                "DELETE FROM ev_event WHERE id = ?",
                id);
    }

    public static class EventMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Event(
                    rs.getLong("id"),
                    rs.getLong("version"),
                    rs.getString("name"),
                    rs.getObject("start_at", LocalDateTime.class),
                    rs.getObject("end_at", LocalDateTime.class),
                    null)
                    ;
        }
    }
}
