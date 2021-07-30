package core.models.user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import core.database.ConnectionFactory;
import interfaces.DAO;

public class UserDAO implements DAO<User> {
    @Override
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

    @Override
    public void update(User user) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "UPDATE users SET " +
                "name = ?, " +
                "cpf = ?, " +
                "online = ? " +
                "WHERE id = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

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

    @Override
    public User findBy(String field, String value) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM users WHERE " + field + " = " + value + " LIMIT 1";
            ResultSet results = statement.executeQuery(sql);

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

    @Override
    public List<User> selectBy(String field, String value) {
        return null;
    }

    @Override
    public List<User> selectAll() {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM users";
            ResultSet results = statement.executeQuery(sql);

            List<User> users = new ArrayList<User>();
            while (results.next()) {
                User user = new User();
                user.setId(results.getLong("id"));
                user.setCpf(results.getString("cpf"));
                user.setName(results.getString("name"));
                user.setPassword(results.getString("password"));
                user.setOnline(results.getInt("online"));

                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User last() {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM users ORDER BY id ASC LIMIT 1";
            ResultSet results = statement.executeQuery(sql);

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
}
