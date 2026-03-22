package top.itsglobally.CircleFramework.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandler {
    void handle(ResultSet rs) throws SQLException;
}