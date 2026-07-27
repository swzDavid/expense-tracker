package de.davidschwietz.expensetracker.controller;

import de.davidschwietz.expensetracker.model.Category;
import de.davidschwietz.expensetracker.model.Transaction;
import de.davidschwietz.expensetracker.model.TransactionType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddTransactionController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<Category> CategoryBox;

    @FXML
    private ComboBox<TransactionType> TypeBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;
}
