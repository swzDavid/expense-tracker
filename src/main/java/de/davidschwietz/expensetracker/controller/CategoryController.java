package de.davidschwietz.expensetracker.controller;

import de.davidschwietz.expensetracker.model.Category;
import de.davidschwietz.expensetracker.service.CategoryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoryController {

    private static final String ADD_CATEGORY_VIEW = "/fxml/AddCategoryView.fxml";

    private final CategoryService categoryService = new CategoryService();
    private final ObservableList<Category> categories = FXCollections.observableArrayList();

    @FXML
    private Button newCategoryButton;
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> nameColumn;
    @FXML
    private TableColumn<Category, Void> actionsColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        // Actions-Spalte: ein Delete-Button pro Zeile
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.getStyleClass().add("delete-button");
                setAlignment(Pos.CENTER);
                deleteButton.setOnAction(e -> deleteCategory(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        categoryTable.setItems(categories);
        loadCategories();
    }

    /** Lädt alle Kategorien neu aus der Datenbank. */
    public void loadCategories() {
        categories.setAll(categoryService.findAllCategories());
    }

    @FXML
    private void handleNewCategory() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ADD_CATEGORY_VIEW));

            Stage dialog = new Stage();
            dialog.setTitle("Add Category");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(newCategoryButton.getScene().getWindow());
            dialog.setScene(new Scene(root));
            dialog.showAndWait(); // wartet, bis Save/Cancel das Fenster schließt

            loadCategories();     // danach Tabelle aktualisieren
        } catch (IOException e) {
            throw new RuntimeException("Could not open AddCategoryView.", e);
        }
    }

    private void deleteCategory(Category category) {
        if (category == null) {
            return;
        }
        try {
            categoryService.deleteCategory(category.getId());
            loadCategories();
        } catch (RuntimeException e) {
            // z. B. wenn die Kategorie noch in Transactions verwendet wird (Fremdschlüssel)
            new Alert(Alert.AlertType.ERROR, "Category could not be deleted.").showAndWait();
        }
    }
}
