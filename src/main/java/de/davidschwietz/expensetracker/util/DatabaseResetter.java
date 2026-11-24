package de.davidschwietz.expensetracker.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseResetter {

    public static void resetDatabase() {
        try (
                Connection connection = DatabaseManager.getConnection();
                Statement statement = connection.createStatement()
        ) {

            statement.executeUpdate(DropTransactions);
            statement.executeUpdate(DropCategories);
            System.out.println("Database delete successful!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String DropTransactions = """
            DROP TABLE IF EXISTS transactions;
            """;

    private static final String DropCategories = """
            DROP TABLE IF EXISTS category;
            """;

}
