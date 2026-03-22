package top.itsglobally.CircleFramework.sql;

import top.itsglobally.CircleFramework.BasePlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseSQL {

    public abstract void connect();

    public abstract void disconnect();

    public abstract boolean isConnected();

    public abstract Connection getConnection();

    public void update(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement ps = prepare(conn, sql, params)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void query(String sql, ResultHandler handler, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement ps = prepare(conn, sql, params);
             ResultSet rs = ps.executeQuery()) {

            handler.handle(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepare(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }

        return ps;
    }
}