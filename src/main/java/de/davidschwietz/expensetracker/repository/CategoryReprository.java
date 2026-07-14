package de.davidschwietz.expensetracker.repository;

import de.davidschwietz.expensetracker.model.Category;
import de.davidschwietz.expensetracker.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryReprository {

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            throw new RuntimeException("Could not load categories.", e);
        }

    }

    public void delete(long id) {

        String sql = """
                DELETE
                FROM category
                WHERE id = ?;
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();

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
