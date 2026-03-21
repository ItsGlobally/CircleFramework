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

    public ResultSet query(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement ps = prepare(conn, sql, params);
        return ps.executeQuery();
    }

    private PreparedStatement prepare(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }

        return ps;
    }

    public void saveJson(String table, String keyColumn, Object key, Object obj) {

        String json = BasePlugin.getGson().toJson(obj);

        String sql;

        if (this instanceof SQLiteImpl) {
            sql = "INSERT OR REPLACE INTO " + table +
                    " (" + keyColumn + ", data) VALUES(?, ?)";
            update(sql, key, json);
        } else {
            sql = "INSERT INTO " + table +
                    " (" + keyColumn + ", data) VALUES(?, ?) " +
                    "ON DUPLICATE KEY UPDATE data=?";
            update(sql, key, json, json);
        }
    }

    public <T> T loadJson(String table, String keyColumn, Object key, Class<T> clazz) {

        try (Connection conn = getConnection();
             ResultSet rs = query(conn,
                     "SELECT data FROM " + table + " WHERE " + keyColumn + "=?",
                     key)) {

            if (rs.next()) {
                return BasePlugin.getGson().fromJson(rs.getString("data"), clazz);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}