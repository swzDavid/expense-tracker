package de.davidschwietz.expensetracker.service;

import de.davidschwietz.expensetracker.model.Transaction;
import de.davidschwietz.expensetracker.repository.TransactionRepository;

import java.util.List;

public class TransactionService {

    private final TransactionRepository repository = new TransactionRepository();

    public void saveTransaction(Transaction transaction) {
        repository.save(transaction);

    }

    public List<Transaction> findAllTransactions() {
        return repository.findAll();

    }

    public Transaction findTransactionById(long id) {
        return repository.findById(id);

    }

    public void deleteTransaction(long id) {
        repository.delete(id);

    }
}
