package top.itsglobally.CircleFramework.sql;

import com.zaxxer.hikari.HikariConfig;

public class MySQLImpl extends HikariSQL {

    private final String host, database, username, password;
    private final int port;

    public MySQLImpl(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    protected HikariConfig createConfig() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&characterEncoding=utf8");

        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(10000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return config;
    }
}