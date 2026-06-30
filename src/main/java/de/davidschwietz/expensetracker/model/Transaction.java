package de.davidschwietz.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private long id;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private TransactionType type;
    private Category category;

    public Transaction(BigDecimal amount, Category category, TransactionType type) {
        validateCategory(category);
        this.category = category;
        validateType(type);
        this.type = type;
        validateAmount(amount);
        this.amount = amount;
        this.date = LocalDate.now();
    }

    public Transaction(BigDecimal amount, Category category, TransactionType type, LocalDate date) {
        this(amount, category, type);
        validateDate(date);
        this.date = date;
    }

    public Transaction(BigDecimal amount, Category category, TransactionType type, String description) {
        this(amount, category, type);
        validateDescription(description);
        this.description = description;
    }

    public Transaction(BigDecimal amount, Category category, TransactionType type, String description, LocalDate date) {
        this(amount, category, type);
        validateDescription(description);
        this.description = description;
        validateDate(date);
        this.date = date;
    }

    public Transaction(BigDecimal amount, Category category, TransactionType type, long id) {
        this(amount, category, type);
        validateId(id);
        this.id = id;
    }

    public Transaction(BigDecimal amount, Category category, TransactionType type, LocalDate date, long id) {
        this(amount, category, type, date);
        validateId(id);
        this.id = id;
    }

    public Transaction(BigDecimal amount, Category category, TransactionType type, String description, long id) {
        this(amount, category, type, description);
        validateId(id);
        this.id = id;
    }

    public Transaction(BigDecimal amount, Category category, TransactionType type, String description, LocalDate date, long id) {
        this(amount, category, type, description, date);
        validateId(id);
        this.id = id;
    }

    private void validateId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id must be greater than 0");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The amount must be a positive number greater than 0, even if it's an expense.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The date lies in the future.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("The description is empty. Optionally, leave the description out.");
        }
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
    }

    private void validateType(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public void setAmount(BigDecimal amount) {
        validateAmount(amount);
        this.amount = amount;
    }

    public void setCategory(Category category) {
        validateCategory(category);
        this.category = category;
    }

    public void setDescription(String description) {
        validateDescription(description);
        this.description = description;
    }

    public void setDate(LocalDate date) {
        validateDate(date);
        this.date = date;
    }

    public void setType(TransactionType type) {
        validateType(type);
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transaction date: " + date
                + "\nThe transaction contains an " + type + " of " + amount
                + "\nCategory: " + category.getName()
                + "\nDescription: " + description;
    }
}
