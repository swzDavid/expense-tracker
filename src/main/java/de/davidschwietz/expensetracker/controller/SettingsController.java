package de.davidschwietz.expensetracker.controller;

import de.davidschwietz.expensetracker.util.DatabaseInitializer;
import de.davidschwietz.expensetracker.util.DatabaseResetter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SettingsController {

    @FXML
    private Button clearButton;

    @FXML
    private void handleClearButton() {
        DatabaseResetter.resetDatabase();
        DatabaseInitializer.initializeDatabase();
    }



}
