package me.floydz69.CraftX1Plus.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by gusta on 02/05/2017.
 */
public abstract class MySQLUtils {

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException  e) {
                e.printStackTrace();
            }
        }

    }

    public void closeStatement(PreparedStatement ps) {
        if (ps != null)
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void closeSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException  e) {
                e.printStackTrace();
            }
        }

    }

    public abstract void createTables();

    public abstract Connection getConnection() throws SQLException;
}

