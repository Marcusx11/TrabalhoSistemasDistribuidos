package core.models.user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import core.database.ConnectionFactory;

public class UserDAO {
    public void create(User user) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "INSERT INTO users (name, cpf, password, online, id) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getCpf());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getOnline());
            stmt.setLong(5, user.getId());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> getUsers(PreparedStatement stmt) throws SQLException {
        List<User> users = new ArrayList<User>();
        ResultSet results = stmt.executeQuery();

        while (results.next()) {
            User user = new User();
            user.setCpf(results.getString("cpf"));
            user.setName(results.getString("name"));
            user.setPassword(results.getString("password"));

            users.add(user);
        }

        results.close();
        stmt.close();

        return users;
    }

    public User findBy(String field, String where) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "UPDATE users SET " +
                "name = ?, " +
                "cpf = ?, " +
                "online = ? " +
                "WHERE id = ?";


            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, field + where);

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getCpf());
            stmt.setInt(3, user.getOnline());
            stmt.setLong(4, user.getId());

            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User findByCpfAndPassword(String cpf, String password) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM users WHERE " + field + " = " + value + " LIMIT 1");

            User user = null;
            while (results.next()) {
                user = new User();
                user.setId(results.getLong("id"));
                user.setCpf(results.getString("cpf"));
                user.setName(results.getString("name"));
                user.setPassword(results.getString("password"));
                user.setOnline(results.getInt("online"));
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> selectBy(String field, String where) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM users");

            List<User> users = new ArrayList<User>();
            while (results.next()) {
                User user = new User();
                user.setId(results.getLong("id"));
                user.setCpf(results.getString("cpf"));
                user.setName(results.getString("name"));
                user.setPassword(results.getString("password"));
                user.setOnline(results.getInt("online"));

            return getUsers(stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> selectAll() {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "SELECT * FROM users";
            PreparedStatement stmt = connection.prepareStatement(sql);
            return getUsers(stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
