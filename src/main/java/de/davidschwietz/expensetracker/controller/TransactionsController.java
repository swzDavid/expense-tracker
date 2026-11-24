package de.davidschwietz.expensetracker.controller;

import de.davidschwietz.expensetracker.model.Transaction;
import de.davidschwietz.expensetracker.service.TransactionService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class TransactionsController {

    private static final double ROW_HEIGHT = 25.0;
    private static final double HEADER_HEIGHT = 28.0;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // ANPASSEN: Name des Einnahmen-Typs in deinem Enum TransactionType (alles andere zählt als Ausgabe)
    private static final String INCOME_TYPE = "INCOME";

    // Pfad relativ zu src/main/resources
    private static final String ADD_TRANSACTION_VIEW = "/fxml/AddTransactionView.fxml";

    private final TransactionService transactionService = new TransactionService();

    @FXML
    private Button addTransactionButton;

    @FXML
    private VBox monthsContainer;

    @FXML
    public void initialize() {
        loadTransactions();
    }

    /** Lädt alle Transactions neu aus der Datenbank und baut pro Monat eine Tabelle. */
    public void loadTransactions() {
        monthsContainer.getChildren().clear();

        List<Transaction> transactions = transactionService.findAllTransactions();

        if (transactions.isEmpty()) {
            monthsContainer.getChildren().add(new Label("No transactions yet."));
            return;
        }

        // Nach Monat gruppieren, neuester Monat zuerst
        Map<YearMonth, List<Transaction>> byMonth = new TreeMap<>(Comparator.reverseOrder());
        for (Transaction t : transactions) {
            byMonth.computeIfAbsent(YearMonth.from(t.getDate()), m -> new ArrayList<>()).add(t);
        }

        byMonth.forEach((month, list) -> {
            String monthName = month.getMonth().getDisplayName(TextStyle.FULL, Locale.GERMAN);
            Label title = new Label(monthName + " " + month.getYear());
            title.getStyleClass().add("month-label");

            Label balanceLabel = new Label("Balance: " + String.format("%.2f€", calculateBalance(list)));
            balanceLabel.getStyleClass().add("balance-label");
            balanceLabel.setMaxWidth(Double.MAX_VALUE);
            balanceLabel.setAlignment(Pos.CENTER_RIGHT);

            VBox section = new VBox(5, title, createTable(list), balanceLabel);
            monthsContainer.getChildren().add(section);
        });
    }

    @FXML
    private void handleAddTransaction() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ADD_TRANSACTION_VIEW));

            Stage dialog = new Stage();
            dialog.setTitle("Add Transaction");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(addTransactionButton.getScene().getWindow());
            dialog.setScene(new Scene(root));
            dialog.showAndWait(); // wartet, bis Save/Cancel das Fenster schließt

            loadTransactions();   // danach Tabellen aktualisieren
        } catch (IOException e) {
            throw new RuntimeException("Could not open AddTransactionView.", e);
        }
    }

    private TableView<Transaction> createTable(List<Transaction> list) {
        TableView<Transaction> table = new TableView<>();
        table.getStyleClass().add("transaction-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getColumns().add(column("Date", t -> t.getDate().format(DATE_FORMAT)));
        table.getColumns().add(column("Description", t -> t.getDescription() == null ? "" : t.getDescription()));
        table.getColumns().add(column("Category", t -> t.getCategory().getName()));
        table.getColumns().add(column("Type", t -> t.getType().name()));

        TableColumn<Transaction, String> amountColumn = column("Amount", t -> String.format("%.2f€", t.getAmount()));
        amountColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        table.getColumns().add(amountColumn);
        table.getColumns().add(createActionsColumn());

        table.getItems().addAll(list);

        // Feste Höhe, damit nur die ScrollPane außen scrollt und nicht jede Tabelle einzeln
        table.setFixedCellSize(ROW_HEIGHT);
        table.setPrefHeight(HEADER_HEIGHT + ROW_HEIGHT * list.size() + 2);
        table.setMinHeight(table.getPrefHeight());
        return table;
    }

    /** Einnahmen minus Ausgaben eines Monats. */
    private BigDecimal calculateBalance(List<Transaction> list) {
        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction t : list) {
            if (INCOME_TYPE.equalsIgnoreCase(t.getType().name())) {
                balance = balance.add(t.getAmount());
            } else {
                balance = balance.subtract(t.getAmount());
            }
        }
        return balance;
    }

    /** Spalte mit einem Delete-Button pro Zeile. */
    private TableColumn<Transaction, Void> createActionsColumn() {
        TableColumn<Transaction, Void> col = new TableColumn<>("Actions");
        col.setMinWidth(70.0);
        col.setPrefWidth(80.0);
        col.setMaxWidth(90.0);
        col.setSortable(false);

        col.setCellFactory(c -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.getStyleClass().add("delete-button");
                setAlignment(Pos.CENTER);
                deleteButton.setOnAction(e -> deleteTransaction(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
        return col;
    }

    private void deleteTransaction(Transaction transaction) {
        if (transaction == null) {
            return;
        }
        try {
            transactionService.deleteTransaction(transaction.getId());
            loadTransactions();
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.ERROR, "Transaction could not be deleted.").showAndWait();
        }
    }

    private TableColumn<Transaction, String> column(String title, Function<Transaction, String> getter) {
        TableColumn<Transaction, String> col = new TableColumn<>(title);
        col.setCellValueFactory(data -> new SimpleStringProperty(getter.apply(data.getValue())));
        return col;
    }
}
