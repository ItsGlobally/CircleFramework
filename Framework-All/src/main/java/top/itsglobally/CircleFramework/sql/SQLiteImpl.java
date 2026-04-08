package top.itsglobally.CircleFramework.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteImpl extends BaseSQL {

    private final File file;
    private String url;
    private boolean connected = false;

    public SQLiteImpl(File file) {
        this.file = file;

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connect() {
        this.url = "jdbc:sqlite:" + file.getAbsolutePath();

        try {
            // 測試連線
            try (Connection conn = DriverManager.getConnection(url)) {
                connected = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connected = false;
        }
    }

    @Override
    public void disconnect() {
        connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public Connection getConnection() {
        try {
            if (!connected) {
                throw new SQLException("Database not connected");
            }
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}