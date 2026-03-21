package top.itsglobally.CircleFramework.sql;

import com.zaxxer.hikari.HikariConfig;

import java.io.File;
import java.io.IOException;

public class SQLiteImpl extends HikariSQL {

    private final File file;

    public SQLiteImpl(File file) {
        this.file = file;
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected HikariConfig createConfig() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());

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