package core.database;
import java.sql.*;

public class Database {
    public static void bootstrap(String id) {

        try (Connection connection = ConnectionFactory.init(id)) {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "name VARCHAR(30), cpf VARCHAR (11), password VARCHAR(15))";
            System.out.println("Create users table");

            connection.prepareStatement(sql).execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
