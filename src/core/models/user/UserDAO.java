package core.models.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import core.database.ConnectionFactory;

public class UserDAO {
    public void create(User user) {
        try (Connection connection = ConnectionFactory.getConnection()) {
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
            String sql = "SELECT * FROM users WHERE ? LIMIT 1";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, field + where);

            return getUsers(stmt).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> selectBy(String field, String where) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "SELECT * FROM users WHERE ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, field + where);

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
