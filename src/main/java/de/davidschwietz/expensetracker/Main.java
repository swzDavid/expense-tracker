package de.davidschwietz.expensetracker;

import de.davidschwietz.expensetracker.util.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
    }
}
