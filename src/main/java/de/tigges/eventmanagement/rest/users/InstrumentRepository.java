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
        return jdbcTemplate.query("select * from ev_instrument order by id asc", new InstrumentMapper());
    }

    public static class InstrumentMapper  implements RowMapper<Instrument> {
        public Instrument mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Instrument.builder()
                    .id(rs.getLong("id"))
                    .instrument(rs.getString("instrument"))
                    .gruppe(rs.getString("gruppe"))
                    .build();
        }
    }
}
