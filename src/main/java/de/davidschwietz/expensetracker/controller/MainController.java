package de.davidschwietz.expensetracker.controller;

import de.davidschwietz.expensetracker.model.Transaction;
import de.davidschwietz.expensetracker.model.TransactionType;
import de.davidschwietz.expensetracker.service.TransactionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MainController {

    private final TransactionService transactionService = new TransactionService();

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, LocalDate> dateColumn;

    @FXML
    private TableColumn<Transaction, String> descriptionColumn;

    @FXML
    private TableColumn<Transaction, String> categoryColumn;

    @FXML
    private TableColumn<Transaction, TransactionType> typeColumn;

    @FXML
    private TableColumn<Transaction, BigDecimal> amountColumn;

    @FXML
    private Label incomeLabel;

    @FXML
    private Label expensesLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Button addTransactionButton;

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button transactionsButton;

    @FXML
    private Button categoriesButton;

    @FXML
    private Button settingsButton;

    @FXML
    private TextField searchField;

    @FXML
    private StackPane contentPane;

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        loadTransactions();
        updateSummary();

    }

    private void loadTransactions() {

        List<Transaction> transactions = transactionService.findAllTransactions();

        ObservableList<Transaction> transactionList = FXCollections.observableArrayList(transactions);

        transactionTable.setItems(transactionList);
    }

    private void loadView(String fxml) throws IOException {
        Parent View = FXMLLoader.load(getClass().getResource("/fxml/" + fxml));

        contentPane.getChildren().setAll(View);
    }


    public void updateSummary() {

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        List<Transaction> transactions = transactionService.findAllTransactions();

        for (Transaction transaction : transactions) {
            if (transaction.getType() == TransactionType.INCOME) {
                income = income.add(transaction.getAmount());
            } else {
                expense = expense.add(transaction.getAmount());
            }
        }

        incomeLabel.setText(income.toPlainString() + " €");
        expensesLabel.setText(expense.toPlainString() + " €");

        BigDecimal balance = income.subtract(expense);
        balanceLabel.setText(balance.toPlainString() + " €");
    }

    @FXML
    private void handleAddTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddTransactionView.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Add Transaction");
            stage.getIcons().add(new Image("/images/plus.png"));
            stage.setScene(scene);
            stage.showAndWait();

            loadTransactions();
            updateSummary();

        } catch (IOException e) {
            throw new RuntimeException("Could not load AddTransaction", e);
        }
    }

    @FXML
    private void handleAddCategory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCategoryView.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Add Category");
            stage.getIcons().add(new Image("/images/plus.png"));
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException("Could not load AddCategory", e);
        }
    }

    @FXML
    private void handleCategories() throws IOException {
        loadView("CategoryView.fxml");
    }

    @FXML
    private void handleDashboard() throws IOException {
        loadView("DashboardView.fxml");
    }

    @FXML
    private void handleTransactions() throws IOException {
        loadView("TransactionsView.fxml");
    }

    @FXML
    private void handleSettings() throws IOException {
        loadView("SettingsView.fxml");
    }

}
