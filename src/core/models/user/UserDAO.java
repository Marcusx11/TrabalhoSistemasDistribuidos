package core.models.user;

import java.sql.*;
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

    public static User findByCpfAndPassword(String cpf, String password) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM users WHERE cpf = " + cpf + " AND password = " + password + " LIMIT 1";

            ResultSet rs = statement.executeQuery(sql);
            User user = new User();

            while (rs.next()) {
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setCpf(rs.getString("cpf"));
                user.setPassword(rs.getString("password"));

                user.get
            }

            return user;

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
