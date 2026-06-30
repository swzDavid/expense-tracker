package de.davidschwietz.expensetracker;

import de.davidschwietz.expensetracker.util.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            Connection connection = DatabaseManager.getConnection();

            System.out.println("Connection established successfully!");

            connection.close();

        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}