package core.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static Connection instance = null;

    public static Connection getInstance() {
        if (instance == null) {
            throw new AssertionError("You have to call init first");
        }

        return instance;
    }

    public synchronized static Connection init(String identifier) {
        if (instance != null) {
            throw new AssertionError("Class ConnectionFactory already initialized");
        }

        try {
            File file = new File("data/bank-" + identifier + ".sqlite");
            if (! file.exists()) {
                file.createNewFile();
            }

            instance = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }

        return instance;
    }
}
