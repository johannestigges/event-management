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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;

    List<Event> findAll() {
        return jdbcTemplate.query("SELECT * from ev_event order by start_at asc", new EventMapper());
    }

    Optional<Event> findByIdAndVersion(Long id, Long version) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * from ev_event WHERE id = ? AND version = ?",
                new EventMapper(), id, version));
    }

    Optional<Event> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * from ev_event WHERE id = ?",
                new EventMapper(), id));
    }

    public Event insert(Event event) {
        var insert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("ev_event")
                .usingGeneratedKeyColumns("id");
        var parameters = new HashMap<String, Object>();
        parameters.put("version", 0);
        parameters.put("name", event.getName());
        parameters.put("start_at", event.getStart());
        parameters.put("end_at", event.getEnd());
        Number id = insert.executeAndReturnKey(parameters);
        return findById(id.longValue()).orElseThrow(RuntimeException::new);
    }

    Event update(Event event) {
        if (jdbcTemplate.update("UPDATE ev_event SET name =? , start_at = ?, end_at = ?, version = ? WHERE id = ? AND version = ?",
                event.getName(), event.getStart(), event.getEnd(), event.getVersion() + 1,
                event.getId(), event.getVersion()) != 1) {
            throw new OptimisticLockingFailureException("Fehler beim Aktualisieren von event " + event);
        }
        return findById(event.getId()).orElseThrow(RuntimeException::new);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM ev_event WHERE id = ?", id);
    }


    public static class EventMapper implements RowMapper<Event> {

        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Event.builder()
                    .id(rs.getLong("id"))
                    .version(rs.getLong("version"))
                    .name(rs.getString("name"))
                    .start(rs.getObject("start_at", LocalDateTime.class))
                    .end(rs.getObject("end_at", LocalDateTime.class))
                    .build();
        }
    }
}
