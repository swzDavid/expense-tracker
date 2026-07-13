package de.davidschwietz.expensetracker.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        try (
                Connection connection = DatabaseManager.getConnection();
                Statement statement = connection.createStatement()
        ) {

            statement.executeUpdate(CategoryTable);
            statement.executeUpdate(TransactionTable);
            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String TransactionTable = """
            CREATE TABLE IF NOT EXISTS transactions(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                type TEXT NOT NULL,
                description TEXT,
                category_id INTEGER NOT NULL,
            
                FOREIGN KEY(category_id) REFERENCES category(id)
            );
            """;

    private static final String CategoryTable = """
            CREATE TABLE IF NOT EXISTS category(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE
            );
            """;

}
