package de.tigges.eventmanagement.rest.users;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InstrumentRepository {

    private final JdbcTemplate jdbcTemplate;

    List<Instrument> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM ev_instrument ORDER BY id asc",
                new InstrumentMapper());
    }

    public static class InstrumentMapper implements RowMapper<Instrument> {
        public Instrument mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Instrument(
                    rs.getLong("id"),
                    rs.getString("instrument"),
                    rs.getString("gruppe"));
        }
    }
}
