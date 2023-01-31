package sg.edu.nus.iss.day22_lecture.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day22_lecture.model.Room;

@Repository
public class RoomRepoImpl implements RoomRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    String countSQL = "select count(*) from room";
    String selectSQL = "select * from room";
    String selectByIdSQL = "select * from room where id = ?";
    String insertSQL = "insert into room (room_type, price) values (?, ?)";
    String updateSQL = "update room set room_type = ?, price = ? where id = ?";
    String deleteSQL = "delete from room where id = ?";

    @Override
    public int count() {
        Integer result = 0;
        result = jdbcTemplate.queryForObject(countSQL, Integer.class);
        if (result == null) {
            return 0;
        } else {
            return result;
        }
    }

    @Override
    public Boolean save(Room room) {
        Boolean saved = false;

        // "insert into room (room_type, price) values ('super deluxe', 500)"
        saved = jdbcTemplate.execute(insertSQL, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, room.getRoomType());
                ps.setInt(2, room.getPrice());
                Boolean rslt = ps.execute();
                return rslt;
            }

        });

        return saved;
    }

    @Override
    public List<Room> findAll() {
        List<Room> rsltList = new ArrayList<Room>();
        rsltList = jdbcTemplate.query(selectSQL, BeanPropertyRowMapper.newInstance(Room.class));

        return rsltList;
    }

    @Override
    public Room findById(Integer id) {
        // "select * from room where id = ?"
        return jdbcTemplate.queryForObject(selectByIdSQL, BeanPropertyRowMapper.newInstance(Room.class), id);
    }

    @Override
    public int update(Room room) {
        int updated = 0;

        // "update room set room_type = ?, price = ? where id = ?";
        updated = jdbcTemplate.update(updateSQL, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, room.getRoomType());
                ps.setInt(2, room.getPrice());
                ps.setInt(3, room.getId());
            }

        });

        return updated;
    }

    @Override
    public int deleteById(Integer id) {
        int deleted = 0;

        PreparedStatementSetter pss = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, id);
            }
        };

        deleted = jdbcTemplate.update(deleteSQL, pss);

        return deleted;
    }

}
