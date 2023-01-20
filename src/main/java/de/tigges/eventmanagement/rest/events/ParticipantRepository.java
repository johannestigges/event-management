package de.tigges.eventmanagement.rest.events;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParticipantRepository {

    private final JdbcTemplate jdbcTemplate;

    public int insert(Long eventId, Long userId, Boolean participate) {
        return jdbcTemplate.update("INSERT INTO ev_participant (event_id, user_id, participate) VALUES(?,?,?)",
                eventId, userId, participate);
    }

    public int update(Long eventId, Long userId, Boolean participate) {
        return jdbcTemplate.update("UPDATE ev_participant SET participate = ? where event_id = ? and user_id = ?",
                participate, eventId, userId);
    }

    public List<Participant> findByEventId(long eventId) {
        return jdbcTemplate.query("select * from ev_participant where event_id = ?", new ParticipantMapper(), eventId);
    }

    public int removeFromEvent(Long eventId) {
        return jdbcTemplate.update("DELETE from ev_participant where event_id = ?", eventId);
    }

    public int removeFromUser(Long user_id) {
        return jdbcTemplate.update("DELETE from ev_participant where user_id = ?", user_id);
    }

    public static class ParticipantMapper implements RowMapper<Participant> {
        public Participant mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Participant.builder()
                    .event_id(rs.getLong("event_id"))
                    .user_id(rs.getLong("user_id"))
                    .participate(rs.getBoolean("participate"))
                    .build();
        }
    }
}
