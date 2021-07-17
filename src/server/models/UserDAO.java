package server.models;

import core.models.User;
import server.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void create(User user) {
        try (Connection connection =ConnectionFactory.getConnection()){

            String sql = "INSERT INTO users (name, cpf, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getCpf());
            stmt.setString(3, user.getPassword());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> getUsers(List<User> users, PreparedStatement stmt) throws SQLException {
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

    public List<User> selectBy(String field, String where) {
        List<User> users = new ArrayList<User>();
        try (Connection connection =ConnectionFactory.getConnection()){
            String sql = "SELECT * FROM users WHERE ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, field + where);

            return getUsers(users, stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> selectAll() {
        List<User> users = new ArrayList<User>();
        try (Connection connection =ConnectionFactory.getConnection()){
            String sql = "SELECT * FROM users";
            PreparedStatement stmt = connection.prepareStatement(sql);
            return getUsers(users, stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
