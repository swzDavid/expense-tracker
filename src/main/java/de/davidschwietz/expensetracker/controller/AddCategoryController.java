package de.davidschwietz.expensetracker.controller;

import de.davidschwietz.expensetracker.model.Category;
import de.davidschwietz.expensetracker.service.CategoryService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCategoryController {

    private final CategoryService categoryService = new CategoryService();

    @FXML
    private TextField nameField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    @FXML
    public void handleCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void handleSaveButton() {
        if (nameField.getText() == null) {
            return;
        }

        try {
            Category category = new Category(nameField.getText());
            categoryService.addCategory(category);

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
