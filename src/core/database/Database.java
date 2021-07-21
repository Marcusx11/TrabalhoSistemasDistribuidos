package core.database;
import java.sql.*;

public class Database {
    public static void bootstrap(String id) {

        try (Connection connection = ConnectionFactory.init(id)) {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(30) NOT NULL, " +
                    "cpf VARCHAR (11) UNIQUE NOT NULL," +
                    "password VARCHAR(15) NOT NULL, " +
                    "online INTEGER NOT NULL DEFAULT(0), " +
                    "balance REAL NOT NULL)";

            System.out.println("Create users table");

            connection.prepareStatement(sql).execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
