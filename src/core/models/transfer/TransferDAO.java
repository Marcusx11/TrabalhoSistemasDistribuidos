package core.models.transfer;

import core.database.ConnectionFactory;
import core.models.user.User;
import interfaces.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransferDAO implements DAO<Transfer> {
    @Override
    public void create(Transfer model) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "INSERT INTO " +
                    "transfers (amount, from_user_id, to_user_id, id) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setDouble(1, model.getAmount());
            stmt.setLong(2, model.getFromUserId());
            stmt.setLong(3, model.getToUserId());
            stmt.setLong(4, model.getId());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Transfer model) {

    }

    @Override
    public Transfer findBy(String field, String value) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM transfers WHERE " + field + " = " + value + " LIMIT 1";
            ResultSet results = statement.executeQuery(sql);

            Transfer transfer = null;
            while (results.next()) {
                transfer = new Transfer();

                transfer.setId(results.getLong("id"));
                transfer.setToUserId(results.getLong("to_user_id"));
                transfer.setFromUserId(results.getLong("from_user_id"));
                transfer.setAmount(results.getDouble("amount"));
            }

            return transfer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Transfer> selectBy(String field, String value) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM transfers WHERE " + field + " = " + value;
            ResultSet results = statement.executeQuery(sql);

            List<Transfer> transfers = new ArrayList<Transfer>();
            while (results.next()) {
                Transfer transfer = new Transfer();
                transfer.setId(results.getLong("id"));
                transfer.setToUserId(results.getLong("to_user_id"));
                transfer.setFromUserId(results.getLong("from_user_id"));
                transfer.setAmount(results.getDouble("amount"));

                transfers.add(transfer);
            }

            return transfers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Transfer> fromUser(String userId) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM transfers WHERE from_user_id = " + userId + " OR to_user_id = " + userId;
            ResultSet results = statement.executeQuery(sql);

            List<Transfer> transfers = new ArrayList<Transfer>();
            while (results.next()) {
                Transfer transfer = new Transfer();
                transfer.setId(results.getLong("id"));
                transfer.setToUserId(results.getLong("to_user_id"));
                transfer.setFromUserId(results.getLong("from_user_id"));
                transfer.setAmount(results.getDouble("amount"));

                transfers.add(transfer);
            }

            return transfers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transfer> selectAll() {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM transfers";
            ResultSet results = statement.executeQuery(sql);

            List<Transfer> transfers = new ArrayList<Transfer>();
            while (results.next()) {
                Transfer transfer = new Transfer();
                transfer.setId(results.getLong("id"));
                transfer.setToUserId(results.getLong("to_user_id"));
                transfer.setFromUserId(results.getLong("from_user_id"));
                transfer.setAmount(results.getDouble("amount"));

                transfers.add(transfer);
            }

            return transfers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
