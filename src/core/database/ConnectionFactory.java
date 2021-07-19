package core.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static String url = null;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    public synchronized static Connection init(String identifier) {
        if (url != null) {
            throw new AssertionError("Class ConnectionFactory already initialized");
        }

        try {
            File file = new File("data/bank-" + identifier + ".sqlite");
            if (! file.exists()) {
                file.createNewFile();
            }

            url = "jdbc:sqlite:" + file.getAbsolutePath();

            return getConnection();
        } catch (IOException | SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
