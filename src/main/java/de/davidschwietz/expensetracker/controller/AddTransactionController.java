package de.davidschwietz.expensetracker.controller;

import de.davidschwietz.expensetracker.model.Category;
import de.davidschwietz.expensetracker.model.Transaction;
import de.davidschwietz.expensetracker.model.TransactionType;
import de.davidschwietz.expensetracker.service.CategoryService;
import de.davidschwietz.expensetracker.service.TransactionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AddTransactionController {

    private final CategoryService categoryService = new CategoryService();
    private final TransactionService transactionService = new TransactionService();

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<Category> categoryBox;

    @FXML
    private ComboBox<TransactionType> typeBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {
        typeBox.getItems().addAll(TransactionType.values());
        datePicker.setValue(LocalDate.now());

        List<Category> categories = categoryService.findAllCategories();
        ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);

        categoryBox.setItems(categoryList);
    }

    @FXML
    private void handleSaveButton() {

        if (amountField.getText().isBlank()
                || categoryBox.getValue() == null
                || typeBox.getValue() == null) {

            return;
        }

        try {
            Transaction transaction = new Transaction(
                    new BigDecimal(amountField.getText()),
                    categoryBox.getValue(), typeBox.getValue(),
                    descriptionField.getText(), datePicker.getValue()
            );

            transactionService.saveTransaction(transaction);

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancelButton() {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
