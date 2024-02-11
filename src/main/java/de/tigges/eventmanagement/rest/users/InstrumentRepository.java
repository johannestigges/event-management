package de.tigges.eventmanagement.rest.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public interface InstrumentRepository extends CrudRepository<Instrument,Long> {

    List<Instrument> findAllByOrderByIdAsc();

     class InstrumentMapper implements RowMapper<Instrument> {
        public Instrument mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Instrument(
                    rs.getLong("id"),
                    rs.getString("instrument"),
                    rs.getString("gruppe"));
        }
    }
}
