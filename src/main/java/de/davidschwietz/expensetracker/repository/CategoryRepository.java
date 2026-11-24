package de.davidschwietz.expensetracker.repository;

import de.davidschwietz.expensetracker.model.Category;
import de.davidschwietz.expensetracker.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    public void save(Category category) {
        String sql = """
                INSERT INTO category(name)
                VALUES (?);
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setString(1, category.getName());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not save category.", e);

        }
    }

    public List<Category> findAll() {
        String sql = """
                SELECT *
                FROM category;
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()

        ) {
            List<Category> categories = new ArrayList<>();

            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                Category category = new Category(id, name);
                categories.add(category);

            }

            return categories;

        } catch (SQLException e) {
            throw new RuntimeException("Could not load categories.", e);

        }
    }

    public Category findById(long id) {
        String sql = """
                SELECT *
                FROM category
                WHERE id = ?;
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String name = result.getString("name");
                return new Category(id, name);

            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Could not find category.", e);

        }
    }

    /** Löscht die Kategorie samt aller zugehörigen Transactions (alles oder nichts). */
    public void delete(long id) {
        String deleteTransactionsSql = """
                DELETE
                FROM transactions
                WHERE category_id = ?;
                """;
        String deleteCategorySql = """
                DELETE
                FROM category
                WHERE id = ?;
                """;

        try (Connection connection = DatabaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try (
                    PreparedStatement deleteTransactions = connection.prepareStatement(deleteTransactionsSql);
                    PreparedStatement deleteCategory = connection.prepareStatement(deleteCategorySql)
            ) {
                deleteTransactions.setLong(1, id);
                deleteTransactions.executeUpdate();

                deleteCategory.setLong(1, id);
                deleteCategory.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not delete category.", e);

        }
    }

    public void update(Category category) {
        String sql = """
                UPDATE category
                SET name = ?
                WHERE id = ?;
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setString(1, category.getName());
            statement.setLong(2, category.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update category.", e);

        }
    }
}