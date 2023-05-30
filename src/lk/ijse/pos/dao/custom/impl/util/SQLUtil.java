package lk.ijse.pos.dao.custom.impl.util;

import lk.ijse.pos.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUtil {

    public static <T> T execute(String sql, Object... args) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject((i + 1), args[i]);
        }
        if (sql.startsWith("SELECT") || sql.startsWith("select")) {
            return (T) preparedStatement.executeQuery();
        } else {
            return (T) new Boolean(preparedStatement.executeUpdate() > 0);
        }
    }
}
