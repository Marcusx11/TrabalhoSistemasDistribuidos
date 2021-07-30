package core.database;
import java.sql.*;

public class Database {
    public static void bootstrap(String identifier) {

        try (Connection connection = ConnectionFactory.init(identifier)) {
            String tableUser = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(30) NOT NULL, " +
                    "cpf VARCHAR (11) UNIQUE NOT NULL," +
                    "password VARCHAR(15) NOT NULL, " +
                    "online INTEGER NOT NULL DEFAULT(0))";

            String tableTransfers = "CREATE TABLE IF NOT EXISTS transfers (" +
                    "id INTEGER NOT NULL PRIMARY KEY, " +
                    "amount REAL NOT NULL, " +
                    "from_user_id INTEGER, " +
                    "to_user_id INTEGER NOT NULL, " +
                    "CONSTRAINT fk_from_user_id FOREIGN KEY (from_user_id) REFERENCES users(id), " +
                    "CONSTRAINT fk_to_user_id FOREIGN KEY (to_user_id) REFERENCES users(id))";

            connection.prepareStatement(tableUser).execute();
            connection.prepareStatement(tableTransfers).execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
