package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        try (Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> select(String sql, PreparedStatementSetter pss, RowMapper<T> rm) throws DataAccessException {
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            rs = pstmt.executeQuery();

            List<T> result = new ArrayList<T>();

            while (rs.next()) {
                result.add(rm.mapRow(rs));
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }
    }

    public <T> T selectForObject(String sql, PreparedStatementSetter pss, RowMapper<T> rm) throws DataAccessException {
        List<T> result = select(sql, pss, rm);

        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
}
