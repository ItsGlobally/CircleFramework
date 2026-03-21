package top.itsglobally.CircleFramework.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class HikariSQL extends BaseSQL {

    protected HikariDataSource dataSource;

    protected abstract HikariConfig createConfig();

    @Override
    public void connect() {
        if (dataSource != null && !dataSource.isClosed()) return;
        this.dataSource = new HikariDataSource(createConfig());
    }

    @Override
    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    @Override
    public boolean isConnected() {
        return dataSource != null && !dataSource.isClosed();
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get SQL connection", e);
        }
    }
}