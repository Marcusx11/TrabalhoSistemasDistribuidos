package server.database;

import core.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() {
        try  {
            return DriverManager.getConnection(Constants.DATABASE_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
