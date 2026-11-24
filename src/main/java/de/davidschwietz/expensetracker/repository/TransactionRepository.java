package de.davidschwietz.expensetracker.repository;

import de.davidschwietz.expensetracker.model.Category;
import de.davidschwietz.expensetracker.model.Transaction;
import de.davidschwietz.expensetracker.model.TransactionType;
import de.davidschwietz.expensetracker.util.DatabaseManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void save(Transaction transaction) {
        String sql = """
                INSERT INTO transactions
                (amount, date, type, description, category_id)
                VALUES (?, ?, ?, ?, ?);
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setBigDecimal(1, transaction.getAmount());
            statement.setString(2, transaction.getDate().toString());
            statement.setString(3, transaction.getType().name());

            if (transaction.getDescription() != null) {
                statement.setString(4, transaction.getDescription());
            } else {
                statement.setNull(4, Types.VARCHAR);
            }

            statement.setLong(5, transaction.getCategory().getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not save transaction.", e);

        }
    }

    public List<Transaction> findAll() {
        String sql = """
                SELECT *
                FROM transactions
                ORDER BY date DESC, id DESC;
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()

        ) {
            List<Transaction> list = new ArrayList<>();

            while (result.next()) {
                long id = result.getLong("id");
                BigDecimal amount = result.getBigDecimal("amount");
                LocalDate date = LocalDate.parse(result.getString("date"));
                TransactionType type = TransactionType.valueOf(result.getString("type"));
                String description = result.getString("description");

                long category_id = result.getLong("category_id");
                CategoryRepository repository = new CategoryRepository();
                Category category = repository.findById(category_id);

                list.add(new Transaction(amount, category, type, description, date, id));

            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Could not find any transcations.", e);

        }
    }

    public Transaction findById(long id) {
        String sql = """
                SELECT *
                FROM transactions
                WHERE id = ?;
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                BigDecimal amount = result.getBigDecimal("amount");
                LocalDate date = LocalDate.parse(result.getString("date"));
                TransactionType type = TransactionType.valueOf(result.getString("type"));
                String description = result.getString("description");

                long category_id = result.getLong("category_id");
                CategoryRepository repository = new CategoryRepository();
                Category category = repository.findById(category_id);

                return new Transaction(amount, category, type, description, date, id);

            }
            return null;


        } catch (SQLException e) {
            throw new RuntimeException("Could not find transaction.", e);
        }
    }

    public void delete(long id) {

        String sql = """
                DELETE
                FROM transactions
                WHERE id = ?;
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not delete transaction.", e);

        }
    }
}
